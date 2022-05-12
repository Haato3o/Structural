from dataclasses import dataclass
from multiprocessing.sharedctypes import Array
import string
from tokenize import Double
from random import randint, random, choice

MAX_LINES = 10_000_000
CHUNK_SIZE = 10_000

def random_date() -> str:
    day = randint(1, 31)
    month = randint(1, 12)
    year = randint(1900, 2022)
    return f"{day:02}{month:02}{year:02}"

def random_string(minLength: int, maxLength: int) -> str:
    return ''.join(choice(string.ascii_letters) for _ in range(randint(minLength, maxLength)))

def random_structure():
    id = randint(0, 9999)
    hello = random_string(0, 15)
    message = random_string(0, 43)
    currency = random_string(2, 2)
    value = int(random() * 10000) * 100
    date = random_date()
    return f"{id:04}{hello:15}{message:43}{currency:2}{value:011}{date:8}"

with open("testcase.txt", "w") as output:
    for _ in range(MAX_LINES // CHUNK_SIZE):
        output.writelines([
            random_structure() for _ in range(CHUNK_SIZE)
        ])
