// Tipos que reflejan los DTOs expuestos por la API del backend.

export type Urgency = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'

export interface TaskResponse {
  id: string
  title: string
  urgency: Urgency
  effort: number
}

export interface MemberResponse {
  id: string
  name: string
  maxCapacity: number
  currentLoad: number
  availableCapacity: number
  tasks: TaskResponse[]
}

export interface AllocationResponse {
  taskId: string
  memberId: string
  memberName: string
  memberCurrentLoad: number
  memberMaxCapacity: number
}

export interface RegisterMemberRequest {
  name: string
  maxCapacity: number
}

export interface SubmitTaskRequest {
  title: string
  urgency: Urgency
  effort: number
}

/** Cuerpo de error estándar que devuelve la API. */
export interface ApiError {
  status: number
  error: string
  message: string
  timestamp: string
  fieldErrors?: Record<string, string>
}
