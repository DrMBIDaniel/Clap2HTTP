# Clap2ESP Android Application

## Purpose

Create an Android application that detects user claps and sends HTTP commands to an ESP32 controller.

The application is designed to control IoT devices such as relays and lights.

---

# Main Features

## 1. Audio Detection

The application must:
- access microphone;
- analyze incoming audio;
- detect clap sounds;
- support background operation.

Initial version:
- detect single clap;
- detect double clap.

Future version:
- user training mode;
- personalized clap recognition using machine learning.

---

## 2. HTTP Communication

The application must send HTTP GET requests.

Examples:

Single clap:
http://ESP32_IP/on


Double clap:
http://ESP32_IP/toggle


The ESP32 IP address must be configurable.

---

## 3. User Interface

Main screen:

- Start/Stop listening button;
- ESP32 IP address field;
- Connection test button;
- Current status display;
- Last detected action.

---

## 4. Background Operation

The application should:
- continue working with screen off;
- use Android foreground service;
- show notification while listening.

---

## 5. Settings

Settings page:

- ESP32 IP address;
- HTTP commands;
- microphone sensitivity;
- clap detection delay.

---

## 6. Future Machine Learning Mode

The application should be designed so that future versions can add:

Training mode:
- record user clap samples;
- create personalized model;
- improve recognition accuracy.

---

# Technology

Target platform:
Android

Preferred language:
Kotlin

Network:
HTTP REST requests

Target device:
ESP32
