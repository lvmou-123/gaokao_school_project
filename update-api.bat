@echo off
chcp 65001 >nul
echo 正在生成 api-docs.yaml ...
call mvn springdoc-openapi:generate
echo 完成！请检查文件变更并执行 git commit
pause
