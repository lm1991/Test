# Test
动态规划
max(n) {
  for(i=1 to n)
    q = max(q, p[i] + max(n - i))
    return q
    }
