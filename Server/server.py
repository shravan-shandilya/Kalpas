#!flask/bin/python
from flask import Flask

app = Flask(__name__)

@app.route("/api/v1/version")
def verion():
	return '{"version":"1.0"}'

if __name__ == "__main__":
	app.run(debug=True,host="0.0.0.0")
