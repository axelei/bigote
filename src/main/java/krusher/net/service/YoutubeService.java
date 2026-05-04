package krusher.net.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

@ApplicationScoped
public class YoutubeService {

    @ConfigProperty(name = "ffmpeg.binary.location", defaultValue = "ffmpeg")
    String ffmpegBinaryLocation;
    @ConfigProperty(name = "yt-dlp.binary.location", defaultValue = "yt-dlp")
    String ytDlpBinaryLocation;

    public String getVideoTitle(String videoUrl) throws IOException {
        Document doc = Jsoup.connect(videoUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .get();

        Element metaTitle = doc.select("meta[property=og:title]").first();

        if (metaTitle != null) {
            return metaTitle.attr("content");
        } else {
            return doc.title();
        }
    }

    public void pipeYoutubeToMp3(String videoUrl, OutputStream targetStream) throws Exception {
        
        ProcessBuilder pbYt = new ProcessBuilder(
                ytDlpBinaryLocation, "-f", "bestaudio", "--no-playlist", "-o", "-", videoUrl
        );

        ProcessBuilder pbFf = new ProcessBuilder(
                ffmpegBinaryLocation, "-i", "pipe:0", "-f", "mp3", "-acodec", "libmp3lame", "pipe:1"
        );

        Process procYt = pbYt.start();
        Process procFf = pbFf.start();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            executor.submit(() -> {
                try (InputStream ytIn = procYt.getInputStream();
                     OutputStream ffIn = procFf.getOutputStream()) {
                    ytIn.transferTo(ffIn);
                } catch (IOException e) {
                    System.err.println("Error en tubería yt-dlp a FFmpeg: " + e.getMessage());
                }
            });

            executor.submit(() -> {
                try (InputStream ffOut = procFf.getInputStream()) {
                    ffOut.transferTo(targetStream);
                    targetStream.flush();
                } catch (IOException e) {
                    System.err.println("Error en tubería FFmpeg a destino: " + e.getMessage());
                }
            });

            executor.submit(() -> procYt.getErrorStream().transferTo(OutputStream.nullOutputStream()));
            executor.submit(() -> procFf.getErrorStream().transferTo(OutputStream.nullOutputStream()));
        } 

        int exitYt = procYt.waitFor();
        int exitFf = procFf.waitFor();

        if (exitYt != 0 || exitFf != 0) {
            throw new RuntimeException("Fallo en procesos. yt-dlp: " + exitYt + ", ffmpeg: " + exitFf);
        }
    }
}
