'use strict';

const md5 = require('crypto-js/md5');
const sha1 = require('crypto-js/sha1');
const hmacSha1 = require('crypto-js/hmac-sha1');
const Base64 = require('crypto-js/enc-base64');

class Crypto {
  static toUTF8Array(str) {
    let utf8 = [];
    for (let i = 0; i < str.length; i++) {
      let charcode = str.charCodeAt(i);
      if (charcode < 0x80) utf8.push(charcode);
      else if (charcode < 0x800) {
        utf8.push(0xc0 | (charcode >> 6),
            0x80 | (charcode & 0x3f));
      }
      else if (charcode < 0xd800 || charcode >= 0xe000) {
        utf8.push(0xe0 | (charcode >> 12),
            0x80 | ((charcode>>6) & 0x3f),
            0x80 | (charcode & 0x3f));
      }
      // surrogate pair
      else {
        i++;
        // UTF-16 encodes 0x10000-0x10FFFF by
        // subtracting 0x10000 and splitting the
        // 20 bits of 0x0-0xFFFFF into two halves
        charcode = 0x10000 + (((charcode & 0x3ff)<<10)
            | (str.charCodeAt(i) & 0x3ff));
        utf8.push(0xf0 | (charcode >>18),
            0x80 | ((charcode>>12) & 0x3f),
            0x80 | ((charcode>>6) & 0x3f),
            0x80 | (charcode & 0x3f));
      }
    }
    return utf8;
  }

  static md5Sign(seed) {
    return md5(seed.toString()).toString();
  }

  static hmacSha1Sign(seed, key) {
    return hmacSha1(seed.toString(), key.toString()).toString();
  }
    
  static obscureWithSalt(content, salt) {
    return Crypto.hmacSha1Sign(content, salt);
  }

  static sha1SignRaw(seed) {
    return sha1(seed);
  }

  static sha1SignToHex(seed) {
    return sha1(seed).toString();
  }
}

// Use "CommonJs `require`" syntax to import for both NodeJsBackend and React16Frontend to guarantee compatibility.
exports.default = Crypto;
