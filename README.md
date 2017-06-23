# Test

max(n) {
  for(i=1 to n)
    q = max(q, p[i] + max(n - i))
    return q
    }
