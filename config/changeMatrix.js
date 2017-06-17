#!/usr/bin/env node
"use strict";

var svgpath = require('./lib/svgpath');
var fs = require('fs');

var path = 'M819.075 16.344h3.35v2.0h-3.35z';

var m = [0.1493,-0.9888,0.9888,0.1493,681.1014,826.312];

var transformed = new svgpath(path)
    .matrix(m)
    .abs()
    .round(1)
    .toString();



// var transformed2 = new svgpath(path1)
//     .translate(30, 0)
//     .scale(10, 10)
//     .abs()
//     .round(1)
//     .toString();

console.log(transformed);
