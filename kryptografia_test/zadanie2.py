import sys
import string
import itertools
import Crypto
from Crypto.Cipher import AES
import base64
import struct
import binascii

def is_printable(text):
    printable = string.printable
    result = True

    for letter in text:
        if letter not in printable:
            result = False

    return result

def str_to_hex(string):
    result = r""

    for i in range(0, len(string), 2):
        result += r"\x%s%s" % (string[i], string[i + 1])

    return result.decode("string_escape")

def is_printable_utf(text):
    try:
        text.decode("utf-8")
    except UnicodeDecodeError as e:
        return False

    return True

if __name__ == "__main__":
    arg = sys.argv[1:]

    ciphertext = arg[0]
    iv = arg[1]
    suffix = arg[2]
    key_space = arg[3]

    