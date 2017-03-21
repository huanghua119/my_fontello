#!/usr/bin/env node
"use strict";

var svgpath = require('./lib/svgpath');

var args = process.argv.splice(2);

if (args.length != 6) {
    console.log("----参数错误 e:svgpath path translateX translateY scaleX scaleY type");
    //console.log(args);
    return;
}

var path = args[0]
var translateX = args[1]
var translateY = args[2]
var scaleX = args[3]
var scaleY = args[4]
var type = parseInt(args[5])

var transformed;

if (type == 1) {
    transformed = new svgpath(path)
    .translate(translateX, translateY)
    .scale(scaleX, scaleY)
    .abs()
    .round(1)
    .toString();
} else if (type == 2) {
    var scaleX1 = parseFloat(scaleX);
    var scaleY1 = parseFloat(scaleY);
    var translateX1 = parseInt(translateX);
    var translateY1 = parseInt(translateY);
    transformed  = new svgpath(path)
    .scale(scaleX1, scaleY1)
    .translate(translateX1, translateY1)
    .abs()
    .round(0)
    .rel()
    .toString();
}


// var transformed2 = new svgpath(path1)
//     .translate(30, 0)
//     .scale(10, 10)
//     .abs()
//     .round(1)
//     .toString();

console.log(transformed);