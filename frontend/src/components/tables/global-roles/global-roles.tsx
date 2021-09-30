import { Component, Host, h, State } from '@stencil/core';
import { ErrorType, GenericResponse, globalRole } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';

// class InsertableGlobalRoleFields {
//   name: string;
//   org: number;
// }
type MutableGlobalRoleFields = Omit<globalRole, 'id' | 'createdBy' | 'modifiedBy' | 'modifiedAt' | 'createdAt'>;
class CreateGlobalRoleRequest {
  insertedFields: MutableGlobalRoleFields;
  email: string;
}
class CreateGlobalRoleResponse extends GenericResponse {
  data: globalRole;
}

// class UpdatableGlobalRoleFields {
//   name: string;
//   org: number;
// }

class UpdateGlobalRoleRequest {
  email: string;
  updatedFields: MutableGlobalRoleFields;
  id: number;
}

class UpdateGlobalRoleResponse extends GenericResponse {
  data: globalRole;
}

class DeleteGlobalRoleRequest {
  id: number;
}

class DeleteGlobalRoleResponse extends GenericResponse {
  data: { id: number };
}

class ReadGlobalRolesResponse extends GenericResponse {
  data: globalRole[];
}

@Component({
  tag: 'global-roles',
  styleUrl: 'global-roles.css',
  shadow: true,
})
export class GlobalRoles {
  defaultFields = { name: '', org: null };
  @State() globalRoles: globalRole[] = [];
  @State() insertedFields: MutableGlobalRoleFields = this.defaultFields;
  @State() updatedFields: MutableGlobalRoleFields = this.defaultFields;
  @State() error: string;
  @State() success: string;
  insertFieldChange(event, fieldName) {
    this.insertedFields[fieldName] = event.target.value;
  }

  updateFieldChange(event, columnName) {
    console.log(this.updatedFields[columnName], event.currentTarget.textContent);
    this.updatedFields[columnName] = event.currentTarget.textContent;
  }
  handleUpdate = async id => {
    console.log(this.updatedFields);
    const result = await fetchAs<UpdateGlobalRoleRequest, UpdateGlobalRoleResponse>('globalroles/update', {
      updatedFields: this.updatedFields,
      email: globals.globalStore.state.email,
      id,
    });
    if (result.error === ErrorType.NoError) {
      this.updatedFields = this.defaultFields;
      this.globalRoles = this.globalRoles.map(globalRole => (globalRole.id === result.data.id ? result.data : globalRole));
      this.success = `Row with id ${result.data.id} updated successfully!`;
    } else {
      console.error('Failed to update row');
      this.error = result.error;
      this.globalRoles = this.globalRoles.map(globalRole => (globalRole.id === result.data?.id ? result.data : globalRole));
    }
  };
  handleDelete = async id => {
    const result = await fetchAs<DeleteGlobalRoleRequest, DeleteGlobalRoleResponse>('globalroles/delete', {
      id,
    });
    if (result.error === ErrorType.NoError) {
      this.success = `Row with id ${result.data.id} deleted successfully!`;
      this.globalRoles = this.globalRoles.filter(globalRole => globalRole.id !== result.data.id);
    } else {
      this.error = result.error;
    }
  };

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    const result = await fetchAs<CreateGlobalRoleRequest, CreateGlobalRoleResponse>('globalroles/create', {
      insertedFields: this.insertedFields,
      email: globals.globalStore.state.email,
    });

    console.log(result);

    if (result.error === ErrorType.NoError) {
      this.insertedFields = this.defaultFields;
      this.globalRoles = this.globalRoles.concat(result.data);
      this.success = `New Row with id ${result.data.id} inserted successfully`;
    } else {
      console.error('Failed to create global role');
      this.error = result.error;
    }
  };

  componentWillLoad() {
    fetchAs<null, ReadGlobalRolesResponse>('globalroles/read', null).then(res => {
      this.globalRoles = res.data;
    });
  }
  render() {
    return (
      <Host>
        <div>{this.error}</div>
        <header>
          <h1>Global Roles</h1>
        </header>
        {/* add flexbox to main -> create and update form should be to the side */}
        <main>
          <form class="form insert-form">
            <div class="form-row">
              <label htmlFor="name" class="label insert-form__label">
                Name
              </label>
              <input type="text" value={this.insertedFields.name} onInput={event => this.insertFieldChange(event, 'name')} class="input insert-form__input" />
            </div>

            <div class="form form-row">
              <label htmlFor="org" class="label insert-form__label">
                Org
              </label>
              <input type="number" value={this.insertedFields.org} onInput={event => this.insertFieldChange(event, 'org')} class="insert-form__input" />
            </div>

            <button onClick={this.handleInsert}>Submit</button>
          </form>
          <table>
            <thead>
              {/* this will be fixed -> on a shared component, this will be passed in and use Map to preserve order */}
              <tr>
                <th>id</th>
                <th>created_at</th>
                <th>created_by</th>
                <th>modified_at</th>
                <th>modified_by</th>
                <th>name</th>
                <th>org</th>
              </tr>
            </thead>
            <tbody>
              {this.globalRoles.map(globalRole => {
                return (
                  <tr>
                    {/* can loop over these as well (using Map to preserve order) */}
                    <td>{globalRole.id}</td>
                    <td>{globalRole.createdAt}</td>
                    <td>{globalRole.createdBy}</td>
                    <td>{globalRole.modifiedAt}</td>
                    <td>{globalRole.modifiedBy}</td>
                    <td
                      contentEditable
                      onInput={event => this.updateFieldChange(event, 'name')}
                      // onKeyPress={this.disableNewlines}
                      // onPaste={this.pasteAsPlainText}
                      // onFocus={this.highlightAll}
                    >
                      {globalRole.name}
                    </td>
                    <td
                      contentEditable
                      onInput={event => this.updateFieldChange(event, 'org')}
                      // onKeyPress={this.disableNewlines}
                      // onPaste={this.pasteAsPlainText}
                      // onFocus={this.highlightAll}
                    >
                      {globalRole.org}
                    </td>
                    <button onClick={() => this.handleUpdate(globalRole.id)}>Update</button>
                    <button onClick={() => this.handleDelete(globalRole.id)}>Delete</button>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </main>
      </Host>
    );
  }
}

// import ReactDOM from 'react-dom'
// import ContentEditable from 'react-contenteditable'
// import { Table, Button } from 'semantic-ui-react'
// import './styles.css'

// class App extends Component {
//   initialState = {
//     store: [
//       { id: 1, item: 'silver', price: 15.41 },
//       { id: 2, item: 'gold', price: 1284.3 },
//       { id: 3, item: 'platinum', price: 834.9 },
//     ],
//     row: {
//       item: '',
//       price: '',
//     },
//   }

//   state = this.initialState
//   firstEditable = React.createRef()

//   addRow = () => {
//     const { store, row } = this.state
//     const trimSpaces = string => {
//       return string
//         .replace(/&nbsp;/g, '')
//         .replace(/&amp;/g, '&')
//         .replace(/&gt;/g, '>')
//         .replace(/&lt;/g, '<')
//     }
//     const trimmedRow = {
//       ...row,
//       item: trimSpacenames(row.item),
//     }

//     row.id = store.length + 1

//     this.setState({
//       store: [...store, trimmedRow],
//       row: this.initialState.row,
//     })

//     this.firstEditable.current.focus()
//   }

//   deleteRow = id => {
//     const { store } = this.state

//     this.setState({
//       store: store.filter(item => id !== item.id),
//     })
//   }

//   disableNewlines = event => {
//     const keyCode = event.keyCode || event.which

//     if (keyCode === 13) {
//       event.returnValue = false
//       if (event.preventDefault) event.preventDefault()
//     }
//   }

//   validateNumber = event => {
//     const keyCode = event.keyCode || event.which
//     const string = String.fromCharCode(keyCode)
//     const regex = /[0-9,]|\./

//     if (!regex.test(string)) {
//       event.returnValue = false
//       if (event.preventDefault) event.preventDefault()
//     }
//   }

//   pasteAsPlainText = event => {
//     event.preventDefault()

//     const text = event.clipboardData.getData('text/plain')
//     document.execCommand('insertHTML', false, text)
//   }

//   highlightAll = () => {
//     setTimeout(() => {
//       document.execCommand('selectAll', false, null)
//     }, 0)
//   }

//   handleContentEditable = event => {
//     const { row } = this.state
//     const {
//       currentTarget: {
//         dataset: { column },
//       },
//       target: { value },
//     } = event

//     this.setState({ row: { ...row, [column]: value } })
//   }

//   handleContentEditableUpdate = event => {
//     const { store } = this.state

//     const {
//       currentTarget: {
//         dataset: { row, column },
//       },
//       target: { value },
//     } = event

//     let updatedRow = store.filter((item, i) => parseInt(i) === parseInt(row))[0]
//     updatedRow[column] = value

//     this.setState({
//       store: store.map((item, i) => (item[column] === row ? updatedRow : item)),
//     })
//   }

//   render() {
//     const {
//       store,
//       row: { item, price },
//     } = this.state

//     return (
//       <div className="App">
//         <h1>React Contenteditable</h1>

//         <Table celled>
//           <Table.Header>
//             <Table.Row>
//               <Table.HeaderCell>Item</Table.HeaderCell>
//               <Table.HeaderCell>Price</Table.HeaderCell>
//               <Table.HeaderCell>Action</Table.HeaderCell>
//             </Table.Row>
//           </Table.Header>
//           <Table.Body>
//             {store.map((row, i) => {
//               return (
//                 <Table.Row key={row.id}>
//                   <Table.Cell className="narrow">
//                     <ContentEditable
//                       html={row.item}
//                       data-column="item"
//                       data-row={i}
//                       className="content-editable"
//                       onKeyPress={this.disableNewlines}
//                       onPaste={this.pasteAsPlainText}
//                       onFocus={this.highlightAll}
//                       onChange={this.handleContentEditableUpdate}
//                     />
//                   </Table.Cell>
//                   <Table.Cell className="narrow">
//                     <ContentEditable
//                       html={row.price.toString()}
//                       data-column="price"
//                       data-row={i}
//                       className="content-editable"
//                       onKeyPress={this.validateNumber}
//                       onPaste={this.pasteAsPlainText}
//                       onFocus={this.highlightAll}
//                       onChange={this.handleContentEditableUpdate}
//                     />
//                   </Table.Cell>
//                   <Table.Cell className="narrow">
//                     <Button
//                       onClick={() => {
//                         this.deleteRow(row.id)
//                       }}
//                     >
//                       Delete
//                     </Button>
//                   </Table.Cell>
//                 </Table.Row>
//               )
//             })}
//             <Table.Row>
//               <Table.Cell className="narrow">
//                 <ContentEditable
//                   html={item}
//                   data-column="item"
//                   className="content-editable"
//                   innerRef={this.firstEditable}
//                   onKeyPress={this.disableNewlines}
//                   onPaste={this.pasteAsPlainText}
//                   onFocus={this.highlightAll}
//                   onChange={this.handleContentEditable}
//                 />
//               </Table.Cell>
//               <Table.Cell className="narrow">
//                 <ContentEditable
//                   html={price}
//                   data-column="price"
//                   className="content-editable"
//                   onKeyPress={this.validateNumber}
//                   onPaste={this.pasteAsPlainText}
//                   onFocus={this.highlightAll}
//                   onChange={this.handleContentEditable}
//                 />
//               </Table.Cell>
//               <Table.Cell className="narrow">
//                 <Button disabled={!item || !price} onClick={this.addRow}>
//                   Add
//                 </Button>
//               </Table.Cell>
//             </Table.Row>
//           </Table.Body>
//         </Table>
//       </div>
//     )
//   }
// }

// ReactDOM.render(<App />, document.getElementById('root'))
