package com.fesi.flowit.note.repository

import com.fesi.flowit.note.entity.Note
import org.springframework.data.jpa.repository.JpaRepository

interface NoteRepository : JpaRepository<Note, Long> {

}
