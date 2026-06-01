#!/bin/bash
# 高考志愿填报助手 - 启动脚本
export JWT_SECRET="your-jwt-secret-here"
export ZHIPU_API_KEY="8d4b29379c5c4e04b419ec0173cf8f68.b5rvETAEnZqjEg7d"
echo "Starting gaokao-advisor..."
mvn spring-boot:run
