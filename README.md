## Meta layer for Mp4 Frame Saver service ##

### Introduction ###

This meta layer is to add the Mp4 Frame Saver service ([Mp4FrameSaver.git](git@github.com:wonbinbk/Mp4FrameSaver.git)) to a Yocto image.

### The recipes ###

Including the following recipes or overriding recipes:

- recipes-thai.phan/mp4-frame-saver/mp4-frame-saver.bb: This is the main recipe to include the main C++ project.

- recipes-support/opencv/opencv_%.bbappend: We need this overriding recipe because the project use OpenCV with ffmpeg backend. So we remove support of gstreamer and add support of libav (ffmpeg forked). The reason for not using gstream to process video and image is because it is more complicated to use than OpenCV.

- recipes-multimedia/ffmpeg/ffmpeg_4.4.1.bb: Although kirkstone already has ffmpeg 5.0.1, we can't directly use this version because it is not API compatible with OpenCV 4.5.5. This recipe was copied from [meta-freescale kirkstone](https://git.yoctoproject.org/meta-freescale/tree/recipes-multimedia/ffmpeg?h=kirkstone).

- recipes-core/images/core-image-thaiphan.bb: To build the final qemux86-64 image. The name of the image is `core-image-thaiphan`.

### Build ###

Clone Yocto and check out `kirkstone` branch:
```bash
git clone --recursive https://github.com/yoctoproject/poky.git poky
cd poky
git checkout kirkstone
```
Then clone this project as a meta-layer of poky
```bash
pwd
# should be inside poky directory
git submodule add meta-thai.phan git@github.com:wonbinbk/meta-thaiphan.git
git submodule init --recursive
```
Source environment
```bash
source oe-init-build-env build
```
A `build` directory should be created and we should be already in it.
Add the newly clone layer to yocto
```bash
cd build
bitbake-layers add-layers ../meta-thai.phan
```
Edit the `build/conf/local.conf` and add these lines on the bottom:
```bash
# By default, qemu image use sysvinit and we want to use systemd
DISTRO_FEATURES:remove = " sysvinit"
DISTRO_FEATURES:append = " systemd"
VIRTUAL-RUNTIME_init_manager = "systemd"
VIRTUAL-RUNTIME_initscripts = ""
# Since we are going to use commercial license from ffmpeg...
LICENSE_FLAGS_ACCEPTED += "commercial"
# As mentioned above, we need ffmpeg 4.4.1, so make sure Yocto prefers this version as well
PREFERRED_VERSION_ffmpeg = "4.4.1"
```

Then we can build
```bash
bitbake core-image-thaiphan
```
With patient (and luck), the image should be built and ready at `build/tmp/deploys/images/qemux86-64` directory. To use it, since we already source environment, we can now use `runqemu`

```bash
runqemu qemux86-64 core-image-thaiphan
```
A virtual machine will boot with that image. Use `root` as login with no password. You should be able to confirm that `mp4FrameSaver` service is running.
```bash
systemctl status mp4FrameSaver
```
A small video file has also been included in the image (by the mp4-frame-saver recipe) for convenient at `/opt/videos/big_buck_bunny.mp4` (otherwise, we will need to use NFS to share files between the host and the virtual machine).
To process that file:
```bash
extractMp4Frames /opt/videos/big_buck_bunny.mp4
```
Logs should be available on the standard output. Also, the service log can be seen in real time with `journal -f &`
