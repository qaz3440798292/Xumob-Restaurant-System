/**
 * Toast 消息提示 - 餐饮行业风格
 * @description 精致的消息提示组件，适合高端餐厅场景
 * @example
 * import { showToast } from '../utils/toast'
 * showToast('登录成功', 'success')
 * showToast('网络异常', 'error')
 * showToast('库存不足', 'warning')
 * showToast('有新订单', 'info')
 */

type ToastType = 'success' | 'error' | 'warning' | 'info'

const defaultDuration = 3000

// 图标映射 - 精致线性图标
const icons: Record<ToastType, string> = {
  success: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M8 12l3 3 5-6"/></svg>`,
  error: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M15 9l-6 6M9 9l6 6"/></svg>`,
  warning: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2L2 22h20L12 2z"/><path d="M12 9v5M12 17v.5"/></svg>`,
  info: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><path d="M12 16v-4M12 8h.01"/></svg>`
}

// 类型配置 - 餐饮暖色调风格
const config: Record<ToastType, { bg: string; border: string; color: string; accent: string }> = {
  success: {
    bg: 'linear-gradient(135deg, #1a2f1a 0%, #0d1f0d 100%)',
    border: '#3d5c3d',
    color: '#a8d5a2',
    accent: '#4ade80'
  },
  error: {
    bg: 'linear-gradient(135deg, #2f1a1a 0%, #1f0d0d 100%)',
    border: '#5c3d3d',
    color: '#f5a8a8',
    accent: '#f87171'
  },
  warning: {
    bg: 'linear-gradient(135deg, #2f2a1a 0%, #1f1a0d 100%)',
    border: '#5c543d',
    color: '#e8d5a2',
    accent: '#fbbf24'
  },
  info: {
    bg: 'linear-gradient(135deg, #1a1a2f 0%, #0d0d1f 100%)',
    border: '#3d3d5c',
    color: '#a8b8f5',
    accent: '#60a5fa'
  }
}

function showToast(message: string, type: ToastType = 'info', duration: number = defaultDuration): void {
  const cfg = config[type]
  const icon = icons[type]

  // 创建容器
  const container = document.createElement('div')
  container.className = 'toast-wrapper'

  // 创建 Toast 元素
  const toast = document.createElement('div')
  toast.className = `toast-card toast-${type}`

  toast.innerHTML = `
    <div class="toast-accent-line"></div>
    <div class="toast-body">
      <div class="toast-icon">${icon}</div>
      <div class="toast-content">
        <p class="toast-message">${message}</p>
      </div>
    </div>
    <button class="toast-close" aria-label="关闭">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round">
        <path d="M18 6L6 18M6 6l12 12"/>
      </svg>
    </button>
  `

  // 卡片样式
  Object.assign(toast.style, {
    position: 'relative',
    display: 'flex',
    flexDirection: 'column',
    padding: '0',
    borderRadius: '8px',
    fontFamily: "'PingFang SC', 'Microsoft YaHei', 'Noto Sans SC', sans-serif",
    boxShadow: `
      0 10px 40px rgba(0, 0, 0, 0.4),
      0 0 0 1px ${cfg.border}40,
      inset 0 1px 0 rgba(255, 255, 255, 0.05)
    `,
    background: cfg.bg,
    border: `1px solid ${cfg.border}60`,
    minWidth: '300px',
    maxWidth: '380px',
    overflow: 'hidden',
    animation: 'toast-rise 0.4s cubic-bezier(0.16, 1, 0.3, 1)'
  })

  // 顶部强调线
  const accentLine = toast.querySelector('.toast-accent-line') as HTMLElement
  Object.assign(accentLine.style, {
    position: 'absolute',
    top: '0',
    left: '0',
    right: '0',
    height: '3px',
    background: `linear-gradient(90deg, transparent, ${cfg.accent}, transparent)`,
    opacity: '0.8'
  })

  // 主体区域
  const body = toast.querySelector('.toast-body') as HTMLElement
  Object.assign(body.style, {
    display: 'flex',
    alignItems: 'center',
    gap: '14px',
    padding: '16px 20px',
    paddingRight: '44px'
  })

  // 图标
  const iconEl = toast.querySelector('.toast-icon') as HTMLElement
  Object.assign(iconEl.style, {
    flexShrink: '0',
    width: '22px',
    height: '22px',
    color: cfg.accent,
    opacity: '0.9'
  })
  iconEl.querySelector('svg')!.style.width = '100%'
  iconEl.querySelector('svg')!.style.height = '100%'

  // 文字
  const content = toast.querySelector('.toast-content') as HTMLElement
  Object.assign(content.style, {
    flex: '1',
    minWidth: '0'
  })

  const msgEl = toast.querySelector('.toast-message') as HTMLElement
  Object.assign(msgEl.style, {
    margin: '0',
    fontSize: '14px',
    fontWeight: '500',
    color: cfg.color,
    letterSpacing: '0.3px',
    lineHeight: '1.4'
  })

  // 关闭按钮
  const closeBtn = toast.querySelector('.toast-close') as HTMLElement
  Object.assign(closeBtn.style, {
    position: 'absolute',
    top: '14px',
    right: '14px',
    width: '18px',
    height: '18px',
    padding: '0',
    border: 'none',
    background: 'transparent',
    cursor: 'pointer',
    color: cfg.color,
    opacity: '0.4',
    transition: 'all 0.2s ease'
  })
  closeBtn.onmouseenter = () => {
    closeBtn.style.opacity = '0.8'
    closeBtn.style.transform = 'rotate(90deg)'
  }
  closeBtn.onmouseleave = () => {
    closeBtn.style.opacity = '0.4'
    closeBtn.style.transform = 'rotate(0deg)'
  }
  closeBtn.onclick = (e) => {
    e.stopPropagation()
    removeToast(container, toast)
  }

  // 容器定位
  Object.assign(container.style, {
    position: 'fixed',
    top: '24px',
    right: '24px',
    zIndex: '9999',
    display: 'flex',
    flexDirection: 'column',
    gap: '12px',
    pointerEvents: 'none'
  })

  container.appendChild(toast)
  document.body.appendChild(container)

  // 添加全局样式
  if (!document.getElementById('toast-restaurant-styles')) {
    const style = document.createElement('style')
    style.id = 'toast-restaurant-styles'
    style.textContent = `
      @keyframes toast-rise {
        from {
          opacity: 0;
          transform: translateY(-20px) scale(0.95);
        }
        to {
          opacity: 1;
          transform: translateY(0) scale(1);
        }
      }
      @keyframes toast-fall {
        from {
          opacity: 1;
          transform: translateY(0) scale(1);
        }
        to {
          opacity: 0;
          transform: translateY(-20px) scale(0.95);
        }
      }
    `
    document.head.appendChild(style)
  }

  // 自动移除
  setTimeout(() => removeToast(container, toast), duration)
}

function removeToast(container: HTMLElement, toast: HTMLElement) {
  toast.style.animation = 'toast-fall 0.3s cubic-bezier(0.16, 1, 0.3, 1) forwards'
  setTimeout(() => {
    if (toast.parentNode) toast.remove()
    if (container.parentNode && container.children.length === 0) container.remove()
  }, 300)
}

export { showToast }
