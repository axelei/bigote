# Bigote

Bigote is a very simple YouTube® music downloader with an HTML interface. Just enter a video URL, and it will download the audio as an MP3 file. I wrote this little app for my father, who sports a great moustache.

![](src/main/resources/META-INF/resources/bigote.jpg | width=500)
**Notes**: AI generated image. This application is for personal or research purposes only.

# Usage

You can build or download a JAR file or a native executable. It requires [ffmpeg](https://www.ffmpeg.org/) and [yt-dlp](https://github.com/yt-dlp/yt-dlp) to be available in your system PATH. Alternatively, you can use a properties file such as:

```
ffmpeg.binary.location=C:/things/ffmpeg/bin/ffmpeg.exe
yt-dlp.binary.location=C:/util/yt-dlp/tools/x64/yt-dlp.exe
```

Then run it with the following parameter: ``-Dsmallrye.config.locations=file:///etc/app/config.properties``

# License

This software is free/libre and is licensed under the [GNU Affero General Public License v3 (AGPLv3)](LICENSE).