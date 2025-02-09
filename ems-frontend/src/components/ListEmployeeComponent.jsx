import React, {useEffect, useState} from 'react'
import { deleteEmployee, getEmployees } from '../services/EmployeeService'
import { useNavigate } from 'react-router-dom'

const ListEmployeeComponent = () => {

    const [employees, setEmployees] = useState([]);
    const navigator = useNavigate();
    useEffect(() => {
        getAllEmployees();
    },[]);

    function getAllEmployees(){
        getEmployees().then((response) => {
            setEmployees(response);
        }).catch(error => {
            console.error(error);        
        });
    }
    function add(){
        navigator('/add-employee');
    }

    function update(id){
        navigator(`/edit-employee/${id}`);
    }
    function remove(id){
        console.log(id);
        deleteEmployee(id).then((response) => {
            getAllEmployees();
        }).catch(error => {
            console.error(error);        
        });
    }
    return (
        <div className='container'>
            <h2 className='text-center'>List of Employees</h2>
            <button className='btn btn-primary mb-2' onClick={add}>Add</button>
            <table className='table table-striped table-bordered'>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        employees.map(employee =>
                            <tr key={employee.id}>
                                <td>{employee.id}</td>
                                <td>{employee.firstName}</td>
                                <td>{employee.lastName}</td>
                                <td>{employee.email}</td>
                                <td>
                                    <button className='btn btn-info' onClick={() => update(employee.id)}>Edit</button>
                                    <button className='btn btn-danger' style={{marginLeft: '7px'}} onClick={() => remove(employee.id)}>Delete</button>
                                </td>
                            </tr>
                        )
                    }
                </tbody>
            </table>
        </div>
    )
}

export default ListEmployeeComponent