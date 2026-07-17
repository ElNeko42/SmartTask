import type {
  AllocationResponse,
  ApiError,
  MemberResponse,
  RegisterMemberRequest,
  SubmitTaskRequest,
} from './types'

const BASE_URL = '/api'

/** Error que transporta el cuerpo {@link ApiError} devuelto por el backend. */
export class ApiRequestError extends Error {
  readonly status: number
  readonly fieldErrors?: Record<string, string>

  constructor(apiError: ApiError) {
    super(apiError.message)
    this.name = 'ApiRequestError'
    this.status = apiError.status
    this.fieldErrors = apiError.fieldErrors
  }
}

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const response = await fetch(`${BASE_URL}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  })

  if (!response.ok) {
    const apiError = (await response.json().catch(() => null)) as ApiError | null
    throw new ApiRequestError(
      apiError ?? {
        status: response.status,
        error: response.statusText,
        message: 'Error inesperado en la petición',
        timestamp: new Date().toISOString(),
      },
    )
  }

  return response.json() as Promise<T>
}

export function listMembers(): Promise<MemberResponse[]> {
  return request<MemberResponse[]>('/members')
}

export function registerMember(body: RegisterMemberRequest): Promise<MemberResponse> {
  return request<MemberResponse>('/members', {
    method: 'POST',
    body: JSON.stringify(body),
  })
}

export function submitTask(body: SubmitTaskRequest): Promise<AllocationResponse> {
  return request<AllocationResponse>('/tasks', {
    method: 'POST',
    body: JSON.stringify(body),
  })
}
