-- ma-ai 项目 MySQL 核心表结构
-- 运行前请确保已选择正确数据库，并已存在 RuoYi 基础表结构

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 用户健康档案
CREATE TABLE IF NOT EXISTS health_profile (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  gender TINYINT DEFAULT NULL COMMENT '0-未知 1-男 2-女',
  birth_date DATE DEFAULT NULL,
  height_cm DECIMAL(5,2) DEFAULT NULL,
  weight_kg DECIMAL(5,2) DEFAULT NULL,
  chronic_diseases VARCHAR(512) DEFAULT NULL,
  allergies VARCHAR(512) DEFAULT NULL,
  medications VARCHAR(512) DEFAULT NULL,
  lifestyle JSON DEFAULT NULL COMMENT '作息、饮食、运动偏好等',
  last_exam JSON DEFAULT NULL COMMENT '最近体检指标',
  remark VARCHAR(512) DEFAULT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_health_profile_user FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户健康档案';

-- 咨询会话
CREATE TABLE IF NOT EXISTS health_consultation (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  consultation_no VARCHAR(64) NOT NULL UNIQUE,
  user_id BIGINT NOT NULL,
  title VARCHAR(128) NOT NULL,
  category VARCHAR(64) DEFAULT NULL COMMENT '症状分类/标签',
  status VARCHAR(32) NOT NULL DEFAULT 'ongoing' COMMENT 'ongoing/completed/closed',
  risk_level VARCHAR(16) DEFAULT 'normal' COMMENT 'normal/warning/urgent',
  ai_model VARCHAR(64) DEFAULT NULL,
  attachments JSON DEFAULT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_health_consultation_user FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康咨询会话';

-- 咨询消息
CREATE TABLE IF NOT EXISTS consultation_message (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  consultation_id BIGINT NOT NULL,
  sender_role VARCHAR(16) NOT NULL COMMENT 'user/assistant/expert',
  message_type VARCHAR(16) NOT NULL DEFAULT 'text' COMMENT 'text/image/document',
  content TEXT NOT NULL,
  tokens_used INT DEFAULT NULL,
  latency_ms INT DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_consult_msg_consult FOREIGN KEY (consultation_id) REFERENCES health_consultation(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康咨询消息';

-- AI 响应日志
CREATE TABLE IF NOT EXISTS ai_inference_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  consultation_id BIGINT DEFAULT NULL,
  user_id BIGINT DEFAULT NULL,
  model VARCHAR(64) NOT NULL,
  prompt_tokens INT DEFAULT NULL,
  completion_tokens INT DEFAULT NULL,
  total_tokens INT DEFAULT NULL,
  latency_ms INT DEFAULT NULL,
  response_status VARCHAR(32) DEFAULT 'success',
  error_message TEXT DEFAULT NULL,
  request_payload JSON DEFAULT NULL,
  response_payload JSON DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_ai_log_user (user_id),
  INDEX idx_ai_log_consult (consultation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 推理调用日志';

-- 运动方案
CREATE TABLE IF NOT EXISTS exercise_plan (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  plan_no VARCHAR(64) NOT NULL UNIQUE,
  user_id BIGINT NOT NULL,
  consultation_id BIGINT DEFAULT NULL,
  title VARCHAR(128) NOT NULL,
  goal VARCHAR(64) NOT NULL,
  duration_weeks INT NOT NULL DEFAULT 4,
  intensity_level VARCHAR(32) DEFAULT 'moderate',
  ai_model VARCHAR(64) DEFAULT NULL,
  summary TEXT,
  precautions TEXT,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_exercise_plan_user FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
  CONSTRAINT fk_exercise_plan_consult FOREIGN KEY (consultation_id) REFERENCES health_consultation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 生成的运动方案';

-- 运动方案日程
CREATE TABLE IF NOT EXISTS exercise_plan_schedule (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  plan_id BIGINT NOT NULL,
  day_index INT NOT NULL COMMENT '第几天，从1开始',
  title VARCHAR(64) NOT NULL,
  activities JSON NOT NULL,
  duration_minutes INT DEFAULT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_plan_schedule_plan FOREIGN KEY (plan_id) REFERENCES exercise_plan(id) ON DELETE CASCADE,
  UNIQUE KEY uk_plan_day (plan_id, day_index)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动方案日程';

-- 运动执行记录
CREATE TABLE IF NOT EXISTS exercise_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  plan_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  schedule_day INT NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'pending' COMMENT 'pending/done/skip',
  start_time DATETIME DEFAULT NULL,
  finish_time DATETIME DEFAULT NULL,
  calorie_burn DECIMAL(8,2) DEFAULT NULL,
  heart_rate JSON DEFAULT NULL,
  remark VARCHAR(512) DEFAULT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_record_plan FOREIGN KEY (plan_id) REFERENCES exercise_plan(id) ON DELETE CASCADE,
  CONSTRAINT fk_record_user FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动执行记录';

-- AI 配置表
CREATE TABLE IF NOT EXISTS ai_config (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  provider VARCHAR(64) NOT NULL COMMENT 'qwen、openai 等',
  model VARCHAR(64) NOT NULL,
  api_key VARCHAR(256) DEFAULT NULL,
  endpoint VARCHAR(256) DEFAULT NULL,
  temperature DECIMAL(3,2) DEFAULT 0.70,
  top_p DECIMAL(3,2) DEFAULT 0.95,
  max_tokens INT DEFAULT 2048,
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1启用 0禁用',
  description VARCHAR(512) DEFAULT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 服务配置';

-- 提示词模板
CREATE TABLE IF NOT EXISTS ai_prompt_template (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  template_code VARCHAR(64) NOT NULL UNIQUE,
  template_name VARCHAR(128) NOT NULL,
  template_type VARCHAR(32) NOT NULL COMMENT 'consultation/exercise/moderation 等',
  language VARCHAR(16) NOT NULL DEFAULT 'zh-CN',
  prompt_text TEXT NOT NULL,
  variables JSON DEFAULT NULL COMMENT '变量说明',
  version VARCHAR(16) DEFAULT '1.0.0',
  status TINYINT NOT NULL DEFAULT 1,
  create_by VARCHAR(64) DEFAULT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_by VARCHAR(64) DEFAULT NULL,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 提示词模板';

SET FOREIGN_KEY_CHECKS = 1;
