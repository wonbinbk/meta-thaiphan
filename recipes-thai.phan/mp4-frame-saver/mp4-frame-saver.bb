SUMMARY = "Mp4 Frame Saver"
DESCRIPTION = "Mp4 Frame Saver systemd service"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "gitsm://github.com/wonbinbk/Mp4FrameSaver.git;protocol=https;branch=master \
           file://mp4FrameSaver.service \
           file://big_buck_bunny.mp4 \
"

SRCREV = "f96c78399aee86441fb918533452475f09166a27"

S = "${WORKDIR}/git"

DEPENDS = "cmake opencv nlohmann-json systemd"

inherit cmake pkgconfig systemd

SYSTEMD_SERVICE:${PN} = "mp4FrameSaver.service"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/mp4FrameSaver ${D}${bindir}/mp4FrameSaver

    install -m 0755 ${B}/extractMp4Frames ${D}${bindir}/extractMp4Frames

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/mp4FrameSaver.service ${D}${systemd_unitdir}/system/mp4FrameSaver.service

    install -d ${D}/opt/videos
    install -m 0644 ${WORKDIR}/big_buck_bunny.mp4 ${D}/opt/videos/big_buck_bunny.mp4
}

FILES:${PN} += "${bindir}"
FILES:${PN} += "${systemd_unitdir}"
FILES:${PN} += "/opt/videos/big_buck_bunny.mp4"

SYSTEMD_AUTO_ENABLE = "enable"

