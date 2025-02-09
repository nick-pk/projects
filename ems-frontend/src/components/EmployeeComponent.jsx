import React, { useEffect, useState } from 'react'
import { createEmployee, getEmployee, updateEmployee } from '../services/EmployeeService'
import { useNavigate, useParams } from 'react-router-dom'
const EmployeeComponent = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');

    const navigator = useNavigate();
    const [errors, settErrors] = useState({
        firstName: '',
        lastName: '',
        email: ''
    })

    const {id} = useParams();
    useEffect(() => {
        if(id){
            getEmployee(id).then((response) => {
                console.log(response);
                setFirstName(response.firstName);
                setLastName(response.lastName);
                setEmail(response.email);
            }).catch(error => {
                console.error(error);
            })
        }
    },[id])
    function save(e){
        e.preventDefault();
        if(validateForm()){
            const employee = {firstName, lastName, email};
            console.log(employee);
            if(id){
                updateEmployee(id,employee).then((response) => {
                    console.log(response);
                    navigator('/employees');
                }).catch(error => {
                    console.error(error);        
                });
            }else{
                createEmployee(employee).then((response) => {
                    console.log(response);
                    navigator('/employees');
                }).catch(error => {
                    console.error(error);        
                });
            }
        }
    }

    function validateForm(){
        let valid = true;
        const copyErrors = {... errors};
        if(firstName.trim()){
            copyErrors.firstName = '';
        }else{
            copyErrors.firstName = 'First name is required';
            valid = false;
        }
        if(lastName.trim()){
            copyErrors.lastName = '';
        }else{
            copyErrors.lastName = 'Last name is required';
            valid = false;
        }
        if(email.trim()){
            copyErrors.email = '';
        }else{
            copyErrors.email = 'Email is required';
            valid = false;
        }
        settErrors(copyErrors);
        
        return valid;
    }

    function getPageTitle(){
        if(id){
            return <h2 className='text-center'>Update Employee Details</h2>
        }
        return <h2 className='text-center'>Add Employee Details</h2>
    }

  return (
    <div className='container'>
        <br/><br/>
        <div className='row'>
            <div className='card col-md-6 offset-md-3 offset-md-3'>
                <br/>
                {
                    getPageTitle()
                }
                <div className='card-body'>
                    <form>
                        <div className='form-group mb-2'>
                        <label className='form-label'>First Name:</label>
                        <input
                            type='text'
                            placeholder='Enter Employee First Name'
                            name='firstName'
                            value={firstName}
                            className={`form-control ${errors.firstName ? 'is-invalid':''}`}
                            onChange={(e) => setFirstName(e.target.value)}>
                        </input>
                        {errors.firstName && <div className='invalid-feedback'>{errors.firstName}</div>}
                        </div>
                        <div className='form-group mb-2'>
                        <label className='form-label'>Last Name:</label>
                        <input
                            type='text'
                            placeholder='Enter Employee Last Name'
                            name='lastName'
                            value={lastName}
                            className={`form-control ${errors.lastName ? 'is-invalid':''}`}
                            onChange={(e) => setLastName(e.target.value)}>
                        </input>
                        {errors.lastName && <div className='invalid-feedback'>{errors.lastName}</div>}
                        </div>
                        <div className='form-group mb-2'>
                        <label className='form-label'>Email:</label>
                        <input
                            type='text'
                            placeholder='Enter Employee Email'
                            name='email'
                            value={email}
                            className={`form-control ${errors.email ? 'is-invalid':''}`}
                            onChange={(e) => setEmail(e.target.value)}>
                        </input>
                        {errors.email && <div className='invalid-feedback'>{errors.email}</div>}
                        </div>
                        <button className='btn btn-success' onClick={save}>Submit</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
  )
}

export default EmployeeComponent