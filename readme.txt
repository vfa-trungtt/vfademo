device="emulator-5554"
adb push 

adb -s $device push img01.jpg /mnt/sdcard/DCIM/img02.jpg
adb -s $device shell am broadcast -a android.intent.action.MEDIA_MOUNTED -d file:///sdcard