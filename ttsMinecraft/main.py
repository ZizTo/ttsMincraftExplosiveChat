# voice_to_minecraft.py (версия 4, надежная)

import speech_recognition as sr
from mcrcon import MCRcon
import time

# --- НАСТРОЙКИ ---
RCON_HOST = "localhost"
RCON_PORT = 25575
RCON_PASSWORD = "123321"
MINECRAFT_NICKNAME = "ZizTo"  # Ваш ник
ENERGY_THRESHOLD = 4000
PAUSE_THRESHOLD = 0.8aa


# --- КОНЕЦ НАСТРОЕК ---

def send_command_to_minecraft(command):
    try:
        with MCRcon(RCON_HOST, RCON_PASSWORD, port=RCON_PORT) as mcr:
            print(f"[RCON] Отправка команды: '{command}'")
            resp = mcr.command(command)
            if resp:
                print(f"[RCON] Ответ сервера: {resp}")
    except Exception as e:
        print(f"[ОШИБКА RCON] Не удалось подключиться или отправить команду: {e}")


def listen_and_recognize():
    recognizer = sr.Recognizer()
    recognizer.energy_threshold = ENERGY_THRESHOLD
    recognizer.pause_threshold = PAUSE_THRESHOLD
    microphone = sr.Microphone()

    print("Настройка микрофона...")
    with microphone as source:
        recognizer.adjust_for_ambient_noise(source, duration=1)

    print(f"Готово. Ваш ник: {MINECRAFT_NICKNAME}. Говорите...")

    while True:
        with microphone as source:
            try:
                audio = recognizer.listen(source)
                print("Распознавание...")
                text = recognizer.recognize_google(audio, language="ru-RU")
                print(f"Распознано: \"{text}\"")

                # --- ИЗМЕНЕНИЕ ЗДЕСЬ ---
                # Формируем простую команду: voicechat <ник> <текст>
                # Кавычки здесь не нужны, так как RCON отправит всё как есть.
                # Также убираем префикс плагина "explosivechat:", он не нужен при вызове команды.
                command = f'voicechat {MINECRAFT_NICKNAME} {text}'

                send_command_to_minecraft(command)

            except sr.UnknownValueError:
                print("Не удалось распознать речь. Попробуйте еще раз.")
            except sr.RequestError as e:
                print(f"Ошибка сервиса распознавания; {e}")
            except Exception as e:
                print(f"Произошла непредвиденная ошибка: {e}")
        time.sleep(0.1)


if __name__ == "__main__":
    listen_and_recognize()