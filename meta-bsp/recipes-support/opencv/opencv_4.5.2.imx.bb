require recipes-support/opencv/opencv_4.5.2.bb

LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRCREV_opencv = "5423d53ae0d116ee5bbe52f8b5503f0cd8586998"
SRCREV_contrib = "f5d7f6712d4ff229ba4f45cf79dfd11c557d56fd"
SRCREV_extra = "855c4528402e563283f86f28c6393f57eb5dcf62"
SRC_URI[tinydnn.md5sum] = "adb1c512e09ca2c7a6faef36f9c53e59"
SRC_URI[tinydnn.sha256sum] = "e2c61ce8c5debaa644121179e9dbdcf83f497f39de853f8dd5175846505aa18b"
SRCREV_FORMAT_append = "_extra"

SRC_URI_remove = " \
    git://github.com/opencv/opencv.git;name=opencv \
"
OPENCV_SRC ?= "git://source.codeaurora.org/external/imx/opencv-imx.git;protocol=https"
SRCBRANCH = "4.5.2_imx"
SRC_URI =+ "${OPENCV_SRC};branch=${SRCBRANCH};name=opencv"
SRC_URI += " \
    git://github.com/opencv/opencv_extra.git;destsuffix=extra;name=extra \
    https://github.com/tiny-dnn/tiny-dnn/archive/v1.0.0a3.tar.gz;destsuffix=git/3rdparty/tinydnn/tiny-dnn-1.0.0a3;name=tinydnn;unpack=false \
    file://OpenCV_DNN_examples.patch \
    file://0001-Add-smaller-version-of-download_models.py.patch;patchdir=../extra \
"

PACKAGECONFIG_remove        = "eigen"
PACKAGECONFIG_append_mx8    = " dnn text"
PACKAGECONFIG_OPENCL        = ""
PACKAGECONFIG_OPENCL_mx8    = "opencl"
PACKAGECONFIG_OPENCL_mx8dxl = ""
PACKAGECONFIG_OPENCL_mx8phantomdxl = ""
PACKAGECONFIG_OPENCL_mx8mm  = ""
PACKAGECONFIG_OPENCL_mx8mnlite  = ""
PACKAGECONFIG_append        = " ${PACKAGECONFIG_OPENCL}"

PACKAGECONFIG[openvx] = "-DWITH_OPENVX=ON -DOPENVX_ROOT=${STAGING_LIBDIR} -DOPENVX_LIB_CANDIDATES='OpenVX;OpenVXU',-DWITH_OPENVX=OFF,virtual/libopenvx,"
PACKAGECONFIG[qt5] = "-DWITH_QT=ON -DOE_QMAKE_PATH_EXTERNAL_HOST_BINS=${STAGING_BINDIR_NATIVE} -DCMAKE_PREFIX_PATH=${STAGING_BINDIR_NATIVE}/cmake,-DWITH_QT=OFF,qtbase qtbase-native,"
PACKAGECONFIG[tests-imx] = "-DINSTALL_TESTS=ON -DOPENCV_TEST_DATA_PATH=${S}/../extra/testdata, -DINSTALL_TESTS=OFF,"

do_unpack_extra_append() {
    mkdir -p ${S}/3rdparty/tinydnn/
    tar xzf ${WORKDIR}/v1.0.0a3.tar.gz -C ${S}/3rdparty/tinydnn/
}

do_install_append() {
    ln -sf opencv4/opencv2 ${D}${includedir}/opencv2
    install -d ${D}${datadir}/OpenCV/samples/data
    cp -r ${S}/samples/data/* ${D}${datadir}/OpenCV/samples/data
    install -d ${D}${datadir}/OpenCV/samples/bin/
    cp -f bin/example_* ${D}${datadir}/OpenCV/samples/bin/
    if ${@bb.utils.contains('PACKAGECONFIG', 'tests-imx', 'true', 'false', d)}; then
        cp -r share/opencv4/testdata/cv/face/* ${D}${datadir}/opencv4/testdata/cv/face/
    fi
}

FILES_${PN}-samples += "${datadir}/OpenCV/samples"
