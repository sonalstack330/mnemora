# Mnemora

An AI-powered voice diary app. Speak your thoughts through a wired earphone mic,
and Mnemora converts them into structured, organized diary entries — with
auto-generated titles, mood detection, and tags — using speech-to-text and an
LLM processing layer.

## Problem it solves

Traditional voice notes apps just dump raw transcripts. Mnemora adds an AI layer
that turns messy spoken thoughts into a properly organized, searchable personal diary.

## Features (planned)

- Voice-to-text diary entry creation via wired earphone mic
- AI-generated title, mood, and tags for each entry
- Full CRUD: create, read, update, delete entries
- Keyword search across entries
- Semantic search (stretch goal) — "when was I stressed about exams"
- Android app frontend

## Tech stack

- **Backend**: Java 25 (Terminus), Spring Boot, Spring Data JPA
- **Database**: PostgreSQL (pgAdmin for management)
- **Frontend**: Android (Java)
- **Speech-to-Text**: Android SpeechRecognizer
- **AI/LLM**: Google Gemini API
- **API testing**: Postman

## Status

🚧 In development — backend skeleton in progress.
- ✅ Backend CRUD (Create, Read, Update, Delete, Search) fully working and tested in Postman
- ✅ Global exception handling (404, 400, 500) with clean JSON responses
- ✅ Gemini API integration working — raw transcript successfully structured into title, content, mood, and tags
- ⏭️ Next: Android app (SpeechRecognizer + UI + API integration)