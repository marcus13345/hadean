#!/usr/bin/env node

const { readdirSync, readFileSync } = require('fs');
const { resolve, parse, relative } = require('path');

const trimWhitespace = true;
const base = "./src";

let lines = 0;
let files = [];

function processDir(path) {
  const children = readdirSync(path, {
    withFileTypes: true
  });
  for(const entity of children) {
    const { name } = entity
    const full = resolve(path, name);
    if(entity.isFile()) {
      processFile(full);
    } else if(entity.isDirectory()) {
      processDir(full);
    }
  }
}

function processFile(path) {
  const parsed = parse(path);
  const rel = relative(resolve(base), path)
  const type = parsed.ext;
  const text = readFileSync(path);
  
  console.log(path);
}

function printStats() {
  console.log("")
}

processDir(base);
printStats();