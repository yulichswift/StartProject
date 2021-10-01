# SharedFlow 取代 LiveData
LiveData 只有在 onResume 狀態才會處理資料, 如果是連續的資料在 onPause 就會遺漏只剩最後一筆. 因此改用 SharedFlow 只有設定只有在 onResume 才收集資料, 並且在 onResume 重新要求資料. 