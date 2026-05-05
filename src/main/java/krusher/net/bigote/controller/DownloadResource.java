package krusher.net.bigote.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import krusher.net.bigote.service.YoutubeService;

@ApplicationScoped
@Path("/download")
public class DownloadResource {

    @Inject
    YoutubeService youtubeService;

    @GET
    @Path("/mp3/")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadMp3(@QueryParam("url") String youtubeUrl) throws Exception {
        String filename = youtubeService.getVideoTitle(youtubeUrl);

        StreamingOutput stream = output -> {
            try {
                youtubeService.pipeYoutubeToMp3(youtubeUrl, output);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        return Response.ok(stream)
                .header("Content-Disposition", "attachment;filename=\"" + filename + ".mp3\"")
                .header("Content-Type", "audio/mpeg")
                .build();
    }
}
