// API 基础配置
const BASE_URL = 'http://localhost:8080/api/v1'

// 登录请求参数
interface LoginParams {
  username: string
  password: string
}

// 注册请求参数
interface RegisterParams {
  username: string
  password: string
  registerType: string
  name?: string
  phone?: string
  positionId?: number
  idCard?: string
  hireDate?: string
  employmentType?: number
}

// 响应数据接口
interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
  timestamp: number
}

// 请求封装
async function request(url: string, options: RequestInit = {}): Promise<ApiResponse> {
  const token = localStorage.getItem('accessToken')
  
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string>)
  }
  
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }
  
  const response = await fetch(`${BASE_URL}${url}`, {
    ...options,
    headers
  })
  
  const data: ApiResponse = await response.json()
  
  if (data.code !== 200) {
    throw new Error(data.message || '请求失败')
  }
  
  return data
}

// 登录
export async function login(username: string, password: string, loginType: string): Promise<ApiResponse> {
  return await request('/auth/login', {
    method: 'POST',
    headers: {
      'X-Login-Type': loginType
    },
    body: JSON.stringify({ username, password } as LoginParams)
  })
}

// 注册
export async function register(data: RegisterParams): Promise<ApiResponse> {
  return await request('/auth/register', {
    method: 'POST',
    body: JSON.stringify(data)
  })
}

// 刷新Token
export async function refreshToken(refreshToken: string, loginType: string): Promise<ApiResponse> {
  return await request('/auth/refresh', {
    method: 'POST',
    headers: {
      'X-Login-Type': loginType
    },
    body: JSON.stringify({ refreshToken })
  })
}
