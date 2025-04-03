INSERT INTO users (created_at, updated_at, email, name, password)
VALUES
    (NOW(), NULL, 'barogo@naver.com', '바로고', '$2a$10$AcOtOsoMKv4bfUqo84o8CuTJbyCuKBlMaRGH70fC4rrpOShVr1sB2');

INSERT INTO delivery (created_at, updated_at, user_id, address, status) VALUES
    (NOW() - INTERVAL 30 DAY, NULL, 1, '서울시 강남구 테헤란로 123', 'DONE'),
    (NOW() - INTERVAL 25 DAY, NOW() - INTERVAL 20 DAY, 1, '서울시 서초구 반포대로 45', 'DONE'),
    (NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 15 DAY, 1, '서울시 송파구 올림픽로 300', 'DONE'),
    (NOW() - INTERVAL 15 DAY, NULL, 1, '서울시 마포구 월드컵북로 396', 'PROGRESSING'),
    (NOW() - INTERVAL 10 DAY, NULL, 1, '서울시 중구 을지로 100', 'PROGRESSING'),
    (NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 5 DAY, 1, '서울시 용산구 한남대로 42', 'CANCELED'),
    (NOW() - INTERVAL 5 DAY, NULL, 1, '서울시 강동구 천호대로 1077', 'READY'),
    (NOW() - INTERVAL 3 DAY, NULL, 1, '서울시 종로구 세종대로 175', 'PROGRESSING'),
    (NOW() - INTERVAL 2 DAY, NULL, 1, '서울시 영등포구 여의대로 108', 'READY'),
    (NOW() - INTERVAL 1 DAY, NULL, 1, '서울시 동작구 노량진로 100', 'READY');
