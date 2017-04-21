#!/usr/bin/env node
"use strict";

var svgpath = require('./lib/svgpath');
var fs = require('fs');

var args = process.argv.splice(2);

if (args.length == 1) {
    var filepath = args[0];
    var data = fs.readFileSync(filepath);
    var index = data.indexOf('\n');
    if (index > -1) {
        var path = data.toString().substring(0, index);
        var para = data.toString().substring(index + 1, data.length);
        var paras = para.split(' ');
        if (paras.length != 5) {
            console.log("文件内容格式错误!");
            return;
        }
        args[0] = path;
        args[1] = paras[0];
        args[2] = paras[1];
        args[3] = paras[2];
        args[4] = paras[3];
        args[5] = paras[4];
    } else {
        console.log("文件内容格式错误!");
        return;
    }
} else if (args.length != 6) {
    console.log("----参数错误 e:svgpath path translateX translateY scaleX scaleY type 或 svgpath filePath");
    //console.log(args);
    return;
}

var path = args[0].replace(/"/g,"").replace(/_/g," ");

var translateX = args[1]
var translateY = args[2]
var scaleX = args[3]
var scaleY = args[4]
var type = parseInt(args[5])

var scaleX1 = parseFloat(scaleX);
var scaleY1 = parseFloat(scaleY);
var translateX1 = parseInt(translateX);
var translateY1 = parseInt(translateY);

var transformed;

if (type == 1) {
    transformed = new svgpath(path)
    .translate(translateX1, translateY1)
    .scale(scaleX1, scaleY1)
    .abs()
    .round(1)
    .toString();
} else if (type == 2) {
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