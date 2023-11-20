<?php
require_once("DocumentParser.php");
echo DocumentParser::parseFromFile(str_replace("$$$$", " ", $argv[1]));