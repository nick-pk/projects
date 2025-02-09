import axios from "axios";

// Get the base URL from environment variable
const BASE_URL = import.meta.env.VITE_API_URL;
const employeeURI = `${BASE_URL}/employees`;

// Log the URL for debugging purposes (remove in production)
console.log("API Base URL:", employeeURI);

// Get all employees
export const getEmployees = async () => {
  try {
    const response = await axios.get(employeeURI);
    return response.data; // Return the data if the request is successful
  } catch (error) {
    console.error("Error fetching employees:", error);
    if (error.response) {
        console.error("Response Error:", error.response);
    }
    if (error.request) {
    console.error("Request Error:", error.request);
    }
    if (error.message) {
    console.error("Error Message:", error.message);
    }
    throw new Error("Failed to fetch employees"); // Throw a custom error for the calling function to handle
  }
};

// Create a new employee
export const createEmployee = async (employee) => {
  try {
    const response = await axios.post(employeeURI, employee);
    return response.data; // Return the created employee data
  } catch (error) {
    console.error("Error creating employee:", error);
    throw new Error("Failed to create employee");
  }
};

// Get a single employee by ID
export const getEmployee = async (id) => {
  try {
    const response = await axios.get(`${employeeURI}/${id}`);
    return response.data; // Return the employee data
  } catch (error) {
    console.error(`Error fetching employee with ID ${id}:`, error);
    throw new Error(`Failed to fetch employee with ID ${id}`);
  }
};

// Update an employee by ID
export const updateEmployee = async (id, employee) => {
  try {
    const response = await axios.put(`${employeeURI}/${id}`, employee);
    return response.data; // Return the updated employee data
  } catch (error) {
    console.error(`Error updating employee with ID ${id}:`, error);
    throw new Error(`Failed to update employee with ID ${id}`);
  }
};

// Delete an employee by ID
export const deleteEmployee = async (id) => {
  try {
    await axios.delete(`${employeeURI}/${id}`); // No response body, just a success/failure status
    return { message: "Employee deleted successfully" }; // Return a success message
  } catch (error) {
    console.error(`Error deleting employee with ID ${id}:`, error);
    throw new Error(`Failed to delete employee with ID ${id}`);
  }
};
