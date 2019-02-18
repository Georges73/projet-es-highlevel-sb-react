import React, { Component } from 'react';

class Productlist extends Component {

  state = {
    isLoading: true,
    groups: []
  }; 

  async componentDidMount() {
    const response = await fetch('http://localhost:8080/products/findAll')
    const body = await response.json();
    this.setState({ groups: body, isLoading: false })

       
        //.catch(err => console.error(err)); 
      }
  
      render() {

        const {groups, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }
        const tableRows = this.state.groups.map((product, index) => 
          <tr key={index}>
            <td>{product.id}</td>
            <td>{product.title}</td>
            <td>{product.description}</td>
            <td>{product.manufacturer}</td>
            <td>{product.price}</td>
          </tr>
        );
      
        return (
          <div className="App">
            <table>
              <tbody>{tableRows}</tbody>
            </table>
          </div>
        );
      }
}

export default Productlist;