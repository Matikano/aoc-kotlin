from collections import deque

grid = [list(line) for line in open("input.txt").read().strip().splitlines()]

for r, row in enumerate(grid):
    for c, item in enumerate(row):
        if item == "S":
            sr = r
            sc = c
            grid[r][c] = "a"
        elif item == "E":
            er = r
            ec = c
            grid[r][c] = "z"

q = deque()

q.append((0, sr, sc))
vis = {(sr, sc)}
while q:
    d, r, c = q.popleft()
    for nr, nc in [(r, c + 1), (r, c - 1), (r + 1, c), (r - 1 , c)]:
        if nr < 0 or nr >= len(grid)  or nc < 0 or nc >= len(grid[0]):
            continue
        if (nr, nc) in vis:
            continue
        if ord(grid[nr][nc]) - ord(grid[r][c]) > 1:
            continue
        if nr == er and nc == ec:
            print(d + 1)
            exit(0)
        vis.add((nr, nc))
        q.append((d + 1, nr, nc))





