package com.example.ffmpegkittestapp

import com.example.ffmpegkit.FFmpegKit
import com.example.ffmpegkit.callbacks.StatisticsCallback
import com.example.ffmpegkittestapp.resources.MR
import okio.FileSystem
import okio.Path

object Encoding {
    var fs: FileSystem? = null
    var context: Any = 0

    suspend fun encodeFiles(directory: Path, output: Path, codec: String,
                            pixelFormat: String, customOptions: String,
                            statisticsCallback: StatisticsCallback) {
        if(fs == null) error("Must provide FileSystem!")
        val resources = listOf(MR.images.machupicchu, MR.images.pyramid,
            MR.images.stonehenge)

        val filenames = listOf("machupicchu", "pyramid", "stonehenge")

        val files = filenames.map { directory.resolve("$it.jpg") }

        resources.zip(files).forEach {
            if(!fs!!.exists(it.second)) it.first.toFile(context, it.second)
        }

        val command = encodingCommand(files, output, codec, pixelFormat, customOptions)
        println("Command is: \n$command")

        FFmpegKit.execute(command, logCallback = {
            println("log: ${it.message}")
        }, statisticsCallback = statisticsCallback)
    }
}

private fun encodingCommand(paths: List<Path>, output: Path, codec: String, pixelFormat: String,
                            customOptions: String) =
    "-hide_banner -y ${pathsInCommand(paths)} -filter_complex \"" +
            "[0:v]setpts=PTS-STARTPTS,scale=w=\'if(gte(iw/ih,640/427),min(iw,640),-1)\':h=\'if(gte(iw/ih,640/427),-1,min(ih,427))\',scale=trunc(iw/2)*2:trunc(ih/2)*2,setsar=sar=1/1,split=2[stream1out1][stream1out2];" +
            "[1:v]setpts=PTS-STARTPTS,scale=w=\'if(gte(iw/ih,640/427),min(iw,640),-1)\':h=\'if(gte(iw/ih,640/427),-1,min(ih,427))\',scale=trunc(iw/2)*2:trunc(ih/2)*2,setsar=sar=1/1,split=2[stream2out1][stream2out2];" +
            "[2:v]setpts=PTS-STARTPTS,scale=w=\'if(gte(iw/ih,640/427),min(iw,640),-1)\':h=\'if(gte(iw/ih,640/427),-1,min(ih,427))\',scale=trunc(iw/2)*2:trunc(ih/2)*2,setsar=sar=1/1,split=2[stream3out1][stream3out2];" +
            "[stream1out1]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=3,select=lte(n\\,90)[stream1overlaid];" +
            "[stream1out2]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=1,select=lte(n\\,30)[stream1ending];" +
            "[stream2out1]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=2,select=lte(n\\,60)[stream2overlaid];" +
            "[stream2out2]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=1,select=lte(n\\,30),split=2[stream2starting][stream2ending];" +
            "[stream3out1]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=2,select=lte(n\\,60)[stream3overlaid];" +
            "[stream3out2]pad=width=640:height=427:x=(640-iw)/2:y=(427-ih)/2:color=#00000000,trim=duration=1,select=lte(n\\,30)[stream3starting];" +
            "[stream2starting][stream1ending]blend=all_expr=\'if(gte(X,(W/2)*T/1)*lte(X,W-(W/2)*T/1),B,A)\':shortest=1[stream2blended];" +
            "[stream3starting][stream2ending]blend=all_expr=\'if(gte(X,(W/2)*T/1)*lte(X,W-(W/2)*T/1),B,A)\':shortest=1[stream3blended];" +
            "[stream1overlaid][stream2blended][stream2overlaid][stream3blended][stream3overlaid]concat=n=5:v=1:a=0,scale=w=640:h=424,format=$pixelFormat[video]\"" +
            " -map [video] -vsync 2 -async 1 $customOptions -c:v ${codec.lowercase()} -r 30 $output"

            private fun pathsInCommand(paths: List<Path>) = buildString {
    paths.forEach {
        append(" -loop 1  -i $it ")
    }
}
