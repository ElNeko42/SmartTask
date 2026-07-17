import { useEffect, useState } from 'react'
import './App.css'
import { ApiRequestError, listMembers, registerMember, submitTask } from './api/client'
import type { MemberResponse, RegisterMemberRequest, SubmitTaskRequest } from './api/types'
import { MemberForm } from './components/MemberForm'
import { TaskForm } from './components/TaskForm'
import { MemberBoard } from './components/MemberBoard'

type Notice = { kind: 'error' | 'success'; text: string } | null

function describeError(error: unknown): string {
  if (error instanceof ApiRequestError) {
    if (error.fieldErrors) {
      return Object.values(error.fieldErrors).join(' · ')
    }
    return error.message
  }
  return 'Ha ocurrido un error inesperado'
}

function App() {
  const [members, setMembers] = useState<MemberResponse[]>([])
  const [notice, setNotice] = useState<Notice>(null)
  const [loading, setLoading] = useState(true)

  async function refresh() {
    try {
      setMembers(await listMembers())
    } catch {
      setNotice({
        kind: 'error',
        text: 'No se pudo cargar el equipo. ¿Está arrancado el backend en el puerto 8080?',
      })
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void refresh()
  }, [])

  async function handleRegister(request: RegisterMemberRequest) {
    try {
      const member = await registerMember(request)
      setNotice({ kind: 'success', text: `Miembro «${member.name}» añadido.` })
      await refresh()
    } catch (error) {
      setNotice({ kind: 'error', text: describeError(error) })
      throw error
    }
  }

  async function handleSubmitTask(request: SubmitTaskRequest) {
    try {
      const result = await submitTask(request)
      setNotice({
        kind: 'success',
        text: `«${request.title}» asignada a ${result.memberName} (${result.memberCurrentLoad}/${result.memberMaxCapacity} pts).`,
      })
      await refresh()
    } catch (error) {
      setNotice({ kind: 'error', text: describeError(error) })
      throw error
    }
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>Smart Task Allocator</h1>
        <p>
          Cada tarea se asigna automáticamente al miembro con menor carga y capacidad
          suficiente. La decisión la toma el dominio.
        </p>
      </header>

      {notice && (
        <div className={`notice ${notice.kind}`} role="status" onClick={() => setNotice(null)}>
          {notice.text}
        </div>
      )}

      <section className="forms">
        <MemberForm onSubmit={handleRegister} />
        <TaskForm onSubmit={handleSubmitTask} />
      </section>

      <section className="team">
        <h2>Equipo</h2>
        {loading ? <p className="loading">Cargando…</p> : <MemberBoard members={members} />}
      </section>
    </div>
  )
}

export default App
