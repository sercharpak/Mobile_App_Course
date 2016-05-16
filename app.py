#!flask/bin/python
from flask import Flask, jsonify, make_response, request, abort, url_for

import numpy as np
import matplotlib.pyplot as plt
import subprocess

app = Flask(__name__)

comando = 'python Classificacion_GPS_Clustering.py'
process =subprocess.Popen(comando,stdout=subprocess.PIPE, stderr=None, shell=True)
resultsString=process.communicate()
print resultsString

data = np.loadtxt('centers.dat')

tasks = []
i=0
for center in data:
    print center
    task = {
    'id': i + 1,
    'latitude': center[0],
    'longitude': center[1]
    }
    tasks.append(task)
    i+=1


@app.route('/todo/api/v1.0/tasks', methods=['POST'])
def create_task():
    if not request.json or not 'latitude' in request.json:
        abort(400)
    task = {
        'id': tasks[-1]['id'] + 1,
        'latitude': request.json['latitude'],
        'longitude': request.json.get('longitude')
    }
    tasks.append(task)
    return jsonify({'task': task}), 201

@app.route('/todo/api/v1.0/tasks/<int:task_id>', methods=['GET'])
def get_task(task_id):
    task = [task for task in tasks if task['id'] == task_id]
    if len(task) == 0:
        abort(404)
    return jsonify({'task': task[0]})

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

@app.route('/todo/api/v1.0/tasks/<int:task_id>', methods=['PUT'])
def update_task(task_id):
    task = [task for task in tasks if task['id'] == task_id]
    if len(task) == 0:
        abort(404)
    if not request.json:
        abort(400)
    if 'latitude' in request.json and type(request.json['latitude']) != unicode:
        abort(400)
    if 'longitude' in request.json and type(request.json['longitude']) is not unicode:
        abort(400)
    task[0]['latitude'] = request.json.get('latitude', task[0]['latitude'])
    task[0]['longitude'] = request.json.get('longitude', task[0]['longitude'])
    return jsonify({'task': task[0]})

@app.route('/todo/api/v1.0/tasks/<int:task_id>', methods=['DELETE'])
def delete_task(task_id):
    task = [task for task in tasks if task['id'] == task_id]
    if len(task) == 0:
        abort(404)
    tasks.remove(task[0])
    return jsonify({'result': True})

def make_public_task(task):
    new_task = {}
    for field in task:
        if field == 'id':
            new_task['uri'] = url_for('get_task', task_id=task['id'], _external=True)
        else:
            new_task[field] = task[field]
    return new_task

@app.route('/todo/api/v1.0/tasks', methods=['GET'])
def get_tasks():
    return jsonify({'tasks': [make_public_task(task) for task in tasks]})

if __name__ == '__main__':
    app.run(debug=True)
