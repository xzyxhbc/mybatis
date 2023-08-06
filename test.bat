@echo off
setlocal enabledelayedexpansion

set "input_folder=D:/aaa/"
set "line_count=6"

for %%F in ("%input_folder%*.csv") do (
    set "filename=%%&#126;nxF"
    set "temp_file=%temp%\!filename!_temp.csv"

    for /f "skip=%line_count% delims=" %%L in ('type "%%F"') do (
        echo %%L >> "!temp_file!"
    )

    move "!temp_file!" "%%F" > nul
    echo 已删除前 !line_count! 行。
)

echo 完成。