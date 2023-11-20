<?php
require_once("vendor/autoload.php");
use PhpOffice\PhpWord\Element\Table;

$phpword = new \PhpOffice\PhpWord\TemplateProcessor($argv[1]);
$json = json_decode(file_get_contents($argv[3]), true);

foreach ($json as $k => $v) {
    if (strpos($k, "clone:") !== false) {
        $phpword->cloneRow(str_replace("clone:", "", $k), (int) $v);
    }
}

foreach ($json as $k => $v) {
    if (is_array($v)) {
        try {
            orient($v['path']);
            resizeImage($v['path']);
        } catch (Exception $e) {
            print "error";
        }

        if (strpos($k, "dummy") !== false) {
            continue;
        }

        if ($k === "imgSignature") {
            list($oWidth, $oHeight) = getimagesize($v['path']);
            if ($oHeight > 100) {
                $v['width'] = 100 * ($oWidth / $oHeight);
                $v['height'] = 100;
            }
        } else {
            $v['ratio'] = true;
            $v['height'] = '';
        }

        $phpword->setImageValue($k, $v);
    } elseif (strpos($k, "clone:") !== false) {

    } else {
        $phpword->setValue($k, $v);
    }
}

$phpword->saveAs($argv[2]);

print "success";

function orient($filename) {
    if (!$filename) {
        return false;
    }


    $exif = exif_read_data($filename);

    if (!isset($exif['Orientation']) && (!isset($exif['IFD0']) || !isset($exif['IFD0']['Orientation']))) {
        print "\n\n no orientation $filename \n\n";
        return false;
    }

    $ort = isset($exif['Orientation']) ? $exif['Orientation'] : $exif['IFD0']['Orientation'];
    switch($ort)
    {
        case 1: // nothing
            break;

        case 2: // horizontal flip
            break;

        case 3: // 180 rotate left
            rotateImage($filename, $filename, 180);
            //rotateImage($filename, $filename . '.rotated', 180);
            return true;

        case 4: // vertical flip
            $image->flipImage($public,2);
            break;

        case 5: // vertical flip + 90 rotate right
            $image->rotateImage($public, -90);
            break;

        case 6: // 90 rotate right
            rotateImage($filename, $filename, -90);
            //rotateImage($filename, $filename . '.rotated', -90);
            return true;

        case 7: // horizontal flip + 90 rotate right
            $image->flipImage($public,1);
            rotateImage($filename, $filename, 90);
            //rotateImage($filename, $filename . '.rotated', 90);
            return true;

        case 8:    // 90 rotate left
            rotateImage($filename, $filename, 90);
            //rotateImage($filename, $filename . '.rotated', 90);
            return true;
    }
}


function rotateImage($filename, $filenameNew, $degrees) {
    $source = imagecreatefromjpeg($filename);
    $rotate = imagerotate($source, $degrees, 0);
    imagejpeg($rotate, $filenameNew);
    imagedestroy($source);
    imagedestroy($rotate);
}


function resizeImage($filename) {
    list($oWidth, $oHeight) = getimagesize($filename);

    if ($oWidth > $oHeight) {
        $width = 1200;
    } else {
        $width = 900;
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
