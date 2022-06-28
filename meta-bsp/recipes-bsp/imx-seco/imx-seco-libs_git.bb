# Copyright 2019-20 NXP

SUMMARY = "NXP i.MX SECO library"
DESCRIPTION = "NXP IMX SECO library"
SECTION = "base"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://EULA.txt;md5=cdd540474054bab41b663abd3e9eb4a0"

DEPENDS = "zlib"

SRCBRANCH = "imx_5.4.70_2.3.8"
SECO_LIB_SRC ?= "git://github.com/NXP/imx-seco-libs.git;protocol=https"
SRC_URI = "${SECO_LIB_SRC};branch=${SRCBRANCH} \
    file://0001-Makefile-Fix-LIBDIR-for-multilib.patch \
    file://0002-Makefile-Fix-install-to-clear-host-user-contaminated.patch \
"
SRCREV = "bbae88af4877fff8df2d62b579794e69d221421c"

S = "${WORKDIR}/git"

TARGET_CC_ARCH += "${LDFLAGS}"

do_install () {
	oe_runmake DESTDIR=${D} install
}

COMPATIBLE_MACHINE = "(mx8)"
COMPATIBLE_MACHINE_mx8m = "(^$)"
