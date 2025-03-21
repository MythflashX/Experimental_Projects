#!/bin/bash

echo ""
echo ""
echo "Run http://localhost:8000/filename.php on web-browser"
echo ""
echo ""

systemctl start mariadb
qutebrowser http://localhost:8000/$1
php -S localhost:8000 -t /home/mytx/onyx_saves/php/url_shortener
