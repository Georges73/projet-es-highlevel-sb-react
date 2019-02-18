import React, { Component } from 'react';

class Productlist extends Component {

    constructor(props) {
        super(props);
        this.state = { products: []};
      } 

      componentDidMount() {
        fetch('http://localhost:8080/products/findAll')
        .then((response) => response.json()) 
        .then((responseData) => { 
          this.setState({ 
            cars: responseData._embedded.products,
          }); 
        })
        .catch(err => console.error(err)); 
      }
  
      render() {
        const tableRows = this.state.products.map((product, index) => 
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