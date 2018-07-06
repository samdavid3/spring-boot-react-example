import * as React from 'react';
import './App.css';

import logo from './logo.svg';

interface Book {
    id: string;
    name: string;
}

interface AppProps {
}

interface AppState {
    books: Array<Book>;
    isLoading: boolean;
}

class App extends React.Component<AppProps, AppState> {

    constructor(props: AppProps) {
        super(props);

        this.state = {
            books: [],
            isLoading: false
        };
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetch('http://localhost:8080/good-books')
            .then(response => response.json())
            .then(data => this.setState({books: data, isLoading: false}));
   }

    render() {
        const {books, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo" />
                    <h1 className="App-title">Welcome to React</h1>
                </header>
                <div>
                    <h2>Good Book List</h2>
                    {books.map((book: Book) =>
                        <div key={book.id}>
                            {book.name}
                        </div>
                    )}
                </div>
            </div>
        );

  }
}

export default App;
