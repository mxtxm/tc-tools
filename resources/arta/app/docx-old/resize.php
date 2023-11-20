<?php
$json = json_decode(file_get_contents($argv[1]), true);

resizeImage($json["f"], $json["x"], $json["y"]);


function resizeImage($filename, $x, $y) {
    list($oWidth, $oHeight) = getimagesize($filename);

    if ($oWidth > $oHeight) {
        $width = $x;
    } else {
        $width = $y;
    }

    if ($oWidth < $width) {
        $oWidth = $width - 5;
    }

    switch (pathinfo($filename)['extension']) {
        case "png":
            return imagepng(imagescale(imagecreatefrompng($filename), $width), $filename);
        case "gif":
            return imagegif(imagescale(imagecreatefromgif($filename), $width), $filename);
        default :
            return imagejpeg(imagescale(imagecreatefromjpeg($filename), $width), $filename);
    }
}
