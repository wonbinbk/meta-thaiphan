DESCRIPTION = "Thai Phan Yocto image for Mp4FrameSaver project"
LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL += "\
    systemd \
    mp4-frame-saver \
    opencv \
    ffmpeg \
"
