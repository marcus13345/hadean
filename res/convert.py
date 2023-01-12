import yaml

dataStr = open("res/names.yml").read()
print("loading data")
data = yaml.safe_load(dataStr)
# data = [
#   {
#     "name": 'fonk',
#     "sex": 'donk'
#   },
#   {
#     "name": 'fonk',
#     "sex": 'donk'
#   },
#   {
#     "name": 'tonk',
#     "sex": 'donk'
#   }
# ]
newData = []

def findPairInArray(arr, n, s):
  for pair in arr:
    name = pair["name"]
    sex = pair["sex"]
    if n == name and s == sex:
      return pair
  return None


for pair in data:
  name = pair["name"]
  sex = pair["sex"]
  print("" + name + " " + sex)
  match = findPairInArray(newData, name, sex)
  print(match)
  if match == None:
    newData.append({
      "name": name,
      "sex": sex
    })

print(newData)

with open('res/output.yaml', 'w') as file:
    outputs = yaml.dump(newData, file)