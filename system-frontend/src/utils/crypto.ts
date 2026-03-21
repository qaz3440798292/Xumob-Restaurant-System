/**
 * RSA 加密工具
 * 
 * 使用后端提供的公钥加密敏感数据（密码等）
 */
import JSEncrypt from 'jsencrypt'

// 公钥缓存
let publicKey: string | null = null

// 初始化 Promise，确保只请求一次
let initPromise: Promise<string> | null = null

/**
 * 初始化加密（获取公钥）
 * 应用启动时调用，之后的调用直接返回缓存的公钥
 */
export async function initEncryption(): Promise<string> {
  if (publicKey) {
    return publicKey
  }
  
  // 如果已经在请求中，返回那个 Promise
  if (initPromise) {
    return initPromise
  }
  
  initPromise = fetchPublicKeyFromServer()
    .then(key => {
      publicKey = key
      return key
    })
    .catch(err => {
      initPromise = null  // 失败后重置，允许重试
      throw err
    })
  
  return initPromise
}

/**
 * 从服务器获取公钥
 */
async function fetchPublicKeyFromServer(): Promise<string> {
  const response = await fetch('http://localhost:8080/api/v1/auth/public-key')
  const result = await response.json()
  
  if (result.code === 200) {
    return result.data
  }
  
  throw new Error(result.message || '获取公钥失败')
}

/**
 * 获取公钥（需先调用 initEncryption）
 */
export function getPublicKey(): string | null {
  return publicKey
}

/**
 * 检查加密是否已初始化
 */
export function isEncryptionReady(): boolean {
  return publicKey !== null
}

/**
 * 等待加密就绪（如果未初始化则等待）
 */
export async function waitForEncryption(): Promise<string> {
  if (publicKey) {
    return publicKey
  }
  return initEncryption()
}

/**
 * 使用 RSA 公钥加密字符串
 * 
 * @param plaintext 明文
 * @param pubKey 公钥（可选，不传则使用缓存的公钥）
 * @returns 加密后的 Base64 字符串
 */
export function encrypt(plaintext: string, pubKey?: string): string {
  const key = pubKey || publicKey
  if (!key) {
    throw new Error('公钥未初始化，请先调用 initEncryption()')
  }
  
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(key)
  const encrypted = encryptor.encrypt(plaintext)
  if (!encrypted) {
    throw new Error('RSA 加密失败')
  }
  return encrypted
}

/**
 * 加密密码（便捷方法）
 * 
 * @param password 明文密码
 * @param pubKey 公钥（可选）
 * @returns 加密后的密码
 */
export function encryptPassword(password: string, pubKey?: string): string {
  return encrypt(password, pubKey)
}

/**
 * 清除缓存的公钥（用于强制重新获取）
 */
export function clearPublicKeyCache(): void {
  publicKey = null
  initPromise = null
}
