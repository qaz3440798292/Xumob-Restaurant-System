<script setup lang="ts">
import { ref, reactive } from 'vue'
import { login, register } from '../api'

// 登录表单接口
interface LoginForm {
  username: string
  password: string
}

// 注册表单接口
interface RegisterForm {
  username: string
  password: string
  confirmPassword: string
  name: string
  phone: string
}

// 登录响应数据接口
interface LoginData {
  accessToken: string
  refreshToken: string
  username: string
  userId: number
  identityType: string
  employeeId?: string
  positionId?: string
  positionCode?: string
  positionName?: string
  positionLevel?: string
}

// 状态
const isRegister = ref(false)
const loading = ref(false)
const errorMsg = ref('')

// 登录表单
const loginForm = reactive<LoginForm>({
  username: '',
  password: ''
})

// 注册表单
const registerForm = reactive<RegisterForm>({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  phone: ''
})

// 登录处理
async function handleLogin(): Promise<void> {
  errorMsg.value = ''
  loading.value = true
  
  try {
    const response = await login(
      loginForm.username,
      loginForm.password,
      'CUSTOMER'
    )
    
    const data = response.data as LoginData
    
    localStorage.setItem('accessToken', data.accessToken)
    localStorage.setItem('refreshToken', data.refreshToken)
    localStorage.setItem('userInfo', JSON.stringify(data))
    
    alert('欢迎回来！')
    
  } catch (error: unknown) {
    const e = error as Error
    errorMsg.value = e.message
  } finally {
    loading.value = false
  }
}

// 注册处理
async function handleRegister(): Promise<void> {
  errorMsg.value = ''
  
  if (registerForm.password !== registerForm.confirmPassword) {
    errorMsg.value = '两次输入的密码不一致'
    return
  }
  
  loading.value = true
  
  try {
    await register({
      username: registerForm.username,
      password: registerForm.password,
      name: registerForm.name,
      phone: registerForm.phone,
      registerType: 'CUSTOMER'
    })
    
    alert('注册成功！')
    isRegister.value = false
    loginForm.username = registerForm.username
    loginForm.password = ''
    
  } catch (error: unknown) {
    const e = error as Error
    errorMsg.value = e.message
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-container">
    <!-- Left Side - Brand -->
    <div class="brand-section">
      <div class="brand-content">
        <div class="brand-logo">
          <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="24" cy="24" r="22" stroke="currentColor" stroke-width="2"/>
            <path d="M16 32V16L24 12L32 16V32" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            <circle cx="24" cy="24" r="6" fill="currentColor"/>
            <path d="M12 32H36" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <h1 class="brand-title">XUMOB</h1>
        <p class="brand-tagline">精致餐饮体验</p>
        
        <div class="feature-list">
          <div class="feature-item">
            <span class="feature-icon">★</span>
            <span>尊享服务</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">★</span>
            <span>实时点餐</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">★</span>
            <span>会员特权</span>
          </div>
        </div>
      </div>
      
      <!-- Decorative elements -->
      <div class="deco-circle deco-1"></div>
      <div class="deco-circle deco-2"></div>
      <div class="deco-line deco-3"></div>
    </div>

    <!-- Right Side - Form -->
    <div class="form-section">
      <div class="form-card">
        <div class="form-header">
          <h2 class="form-title">{{ isRegister ? '创建账号' : '欢迎回来' }}</h2>
          <p class="form-subtitle">
            {{ isRegister ? '加入我们，享受精致美食' : '登录后继续' }}
          </p>
        </div>

        <!-- Tab Switcher -->
        <div class="tab-switch">
          <button 
            :class="['tab-item', { active: !isRegister }]"
            @click="isRegister = false"
          >
            登录
          </button>
          <button 
            :class="['tab-item', { active: isRegister }]"
            @click="isRegister = true"
          >
            注册
          </button>
        </div>

        <!-- Login Form -->
        <form v-if="!isRegister" @submit.prevent="handleLogin" class="auth-form">
          <div class="input-group">
            <input 
              v-model="loginForm.username"
              type="text" 
              class="form-input" 
              placeholder="用户名"
              required
            />
          </div>

          <div class="input-group">
            <input 
              v-model="loginForm.password"
              type="password" 
              class="form-input" 
              placeholder="密码"
              required
            />
          </div>

          <div class="form-options">
            <label class="remember-me">
              <input type="checkbox" />
              <span>记住我</span>
            </label>
            <a href="#" class="forgot-link">忘记密码？</a>
          </div>

          <button type="submit" class="submit-btn" :disabled="loading">
            {{ loading ? '登录中...' : '登 录' }}
          </button>
        </form>

        <!-- Register Form -->
        <form v-else @submit.prevent="handleRegister" class="auth-form">
          <div class="input-row">
            <div class="input-group">
              <input 
                v-model="registerForm.name"
                type="text" 
                class="form-input" 
                placeholder="姓名"
              />
            </div>
          </div>

          <div class="input-group">
            <input 
              v-model="registerForm.username"
              type="text" 
              class="form-input" 
              placeholder="用户名"
              required
            />
          </div>

          <div class="input-group">
            <input 
              v-model="registerForm.phone"
              type="tel" 
              class="form-input" 
              placeholder="手机号"
            />
          </div>

          <div class="input-row">
            <div class="input-group">
              <input 
                v-model="registerForm.password"
                type="password" 
                class="form-input" 
                placeholder="密码"
                required
              />
            </div>
          </div>

          <div class="input-group">
            <input 
              v-model="registerForm.confirmPassword"
              type="password" 
              class="form-input" 
              placeholder="确认密码"
              required
            />
          </div>

          <button type="submit" class="submit-btn" :disabled="loading">
            {{ loading ? '注册中...' : '创 建 账 号' }}
          </button>
        </form>

        <!-- Error Message -->
        <div v-if="errorMsg" class="error-toast">
          {{ errorMsg }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Restaurant Theme - Elegant & Warm */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.auth-container {
  min-height: 100vh;
  display: flex;
  font-family: -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

/* Left Side - Brand */
.brand-section {
  flex: 1;
  background: linear-gradient(135deg, #1a1512 0%, #2d2520 50%, #1a1512 100%);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.brand-content {
  position: relative;
  z-index: 10;
  text-align: center;
  padding: 40px;
}

.brand-logo {
  width: 60px;
  height: 60px;
  margin: 0 auto 20px;
  color: #d4a574;
}

.brand-logo svg {
  width: 100%;
  height: 100%;
}

.brand-title {
  font-size: 36px;
  font-weight: 700;
  color: #f5f0eb;
  letter-spacing: 6px;
  margin-bottom: 8px;
}

.brand-tagline {
  font-size: 12px;
  color: #a09080;
  letter-spacing: 2px;
  text-transform: uppercase;
  margin-bottom: 32px;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.feature-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: #d4a574;
  font-size: 13px;
  letter-spacing: 1px;
}

.feature-icon {
  font-size: 10px;
}

/* Decorative Elements */
.deco-circle {
  position: absolute;
  border-radius: 50%;
  border: 1px solid rgba(212, 165, 116, 0.1);
}

.deco-1 {
  width: 400px;
  height: 400px;
  top: -100px;
  left: -100px;
}

.deco-2 {
  width: 300px;
  height: 300px;
  bottom: -50px;
  right: -50px;
}

.deco-line {
  position: absolute;
  width: 1px;
  background: linear-gradient(to bottom, transparent, rgba(212, 165, 116, 0.3), transparent);
}

.deco-3 {
  height: 200px;
  top: 50%;
  left: 30%;
}

/* Right Side - Form */
.form-section {
  flex: 1;
  background: #faf8f5;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.form-card {
  width: 100%;
  max-width: 420px;
}

.form-header {
  text-align: center;
  margin-bottom: 32px;
}

.form-title {
  font-size: 28px;
  font-weight: 600;
  color: #2d2520;
  margin-bottom: 8px;
}

.form-subtitle {
  font-size: 14px;
  color: #8a8077;
}

/* Tab Switcher */
.tab-switch {
  display: flex;
  background: #f0ebe5;
  border-radius: 30px;
  padding: 4px;
  margin-bottom: 32px;
}

.tab-item {
  flex: 1;
  padding: 12px;
  border: none;
  background: transparent;
  font-size: 15px;
  font-weight: 500;
  color: #8a8077;
  cursor: pointer;
  border-radius: 26px;
  transition: all 0.3s ease;
  font-family: -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.tab-item.active {
  background: #fff;
  color: #2d2520;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* Form */
.auth-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.input-group {
  width: 100%;
}

.input-row {
  display: flex;
  gap: 12px;
}

.form-input {
  width: 100%;
  padding: 16px 20px;
  border: 1px solid #e5e0da;
  border-radius: 8px;
  font-size: 14px;
  color: #2d2520;
  background: #fff;
  transition: all 0.3s ease;
  font-family: -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.form-input:focus {
  outline: none;
  border-color: #d4a574;
  box-shadow: 0 0 0 3px rgba(212, 165, 116, 0.1);
}

.form-input::placeholder {
  color: #b5afa6;
}

/* Form Options */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #8a8077;
  cursor: pointer;
}

.remember-me input {
  accent-color: #d4a574;
}

.forgot-link {
  color: #d4a574;
  text-decoration: none;
  transition: color 0.3s ease;
}

.forgot-link:hover {
  color: #2d2520;
}

/* Submit Button */
.submit-btn {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #d4a574 0%, #c4956a 100%);
  border: none;
  border-radius: 8px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 8px;
  font-family: -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Microsoft YaHei', sans-serif;
  letter-spacing: 2px;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(212, 165, 116, 0.3);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Error Toast */
.error-toast {
  margin-top: 20px;
  padding: 14px;
  background: #fff5f5;
  border: 1px solid #ffcdd2;
  border-radius: 8px;
  color: #c62828;
  font-size: 13px;
  text-align: center;
}

/* Responsive */
@media (max-width: 900px) {
  .brand-section {
    display: none;
  }
  
  .form-section {
    flex: none;
    width: 100%;
  }
}
</style>
