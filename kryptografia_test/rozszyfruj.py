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

def decode(iv, suffix, ciphertext_file):
    possibilities = []
    ciphertext_in_base64 = r""

    temp_iv = str_to_hex(iv)

    # read the ciphertext
    with open(ciphertext_file, "r") as ciphertext:
        file_content = ciphertext.read().strip()

        ciphertext_in_base64 = file_content

    ciphertext_in_base64 = base64.b64decode(ciphertext_in_base64)

    key_chars = "0123456789abcdef"
    prefix_len = 64 - len(suffix)

    prefix_gen = itertools.product(key_chars, repeat=prefix_len)

    for prefix in prefix_gen:
        temp_key = "".join(prefix) + suffix
        possible_key = str_to_hex(temp_key)

        decryption_suite = AES.new(possible_key, AES.MODE_CBC, temp_iv)
        plain_text = decryption_suite.decrypt(ciphertext_in_base64)

        if is_printable_utf(plain_text[:-16]):
            possibilities.append(temp_key)

    return possibilities

if __name__ == "__main__":
    binary_text = []
    text_in_ascii = ""

    iv_table = [ "7541003e51d627d4e5bbe976b920699b", "65a33f4ca32c9e6f3557abb3d925a70b",
                 "b4f2e3747e80727c6f320f852ad5c42c", "6f83067c97277aed29d30090008b361a",
                 "e756ea98d2bfad4936bd4c7b5638b6aa", "dddd79ec16743718042ddcf77edad842",
                 "c6feea057ed4ea031540718ee405d57a", "78a2d340bc1be40dd55d754fbb4318da",
                 "d4ab9c2d9b60354db6bf9bd0ca66fd04" ]

    suffix_tables = [ "11b0659c688380d2e0a702d4bac3909cf611cf63e82cfe79c1654456",
                      "78e1dd6c96f53411eb2f8424876ad6eb012a2e14a1aeefb2dce91aa6",
                      "dfa2be1695949fad21dfddcaf4e3bf093cd7d9002c347be55061036",
                      "a1c5508cc4f899af2792f188463ec3862482c5cd74af5d6faddcf7",
                      "ede1ab825708c770d39a4b050b935857842cee2f4f8cd9fb1c897",
                      "bf262401b7b8194760da35dca1f90dcb1dcb22184db4386ee456",
                      "6fa98d56c5f114db5d09198a55cd856ffa1cc92c577c0f8f706",
                      "9f54ad9a6592fb85ed4ece24382ee2c6e15bcd406a54015430",
                      "8a6dcb01b581bf56fcf2a4ca724ca63eea6f11390a3d9b547" ]

    for index in range(len(iv_table)):
        possible_keys = decode(iv_table[index], suffix_tables[index], "szyfrogram" + str(index + 1))

        print "SZYFROGRAM nr: " + str(index + 1)
        for key in possible_keys:
            print key

        print ""

