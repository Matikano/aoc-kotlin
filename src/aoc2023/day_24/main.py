import sympy

hailstones = [tuple(map(int, line.replace("@", ",").split(","))) for line in open("input.txt")]

xr, yr, zr, vxr, vyr, vzr = sympy.symbols("xr, yr, zr, vxr, vyr, vzr")

equations = []

for i, (x, y, z, vx, vy, vz) in enumerate(hailstones):
    equations.append((xr - x) * (vy - vyr) - (yr - y) * (vx - vxr))
    equations.append((yr - y) * (vz - vzr) - (zr - z) * (vy - vyr))

    if i < 2:
        continue

    #   Appending only integer solutions
    answers = [sol for sol in sympy.solve(equations) if all(a % 1 == 0 for a in sol.values())]

    if len(answers) == 1:
        break

answer = answers[0]
result = answer[xr] + answer[yr] + answer[zr]
print(answer)
print("Result = ", result)
