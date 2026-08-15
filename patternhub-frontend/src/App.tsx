import React, {useEffect, useState} from 'react'
import axios from 'axios'
import './App.css'

type Request = {
  id?: number
  description: string
  priority: string
  type: string
  user?: { id?: number; name?: string; email?: string }
}

type User = {
  id?: number
  name: string
  email: string
}

export default function App(){
  const [requests, setRequests] = useState<Request[]>([])
  const [users, setUsers] = useState<User[]>([])
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [editingUserId, setEditingUserId] = useState<number | null>(null)
  const [reqType, setReqType] = useState('GENERAL')
  const [reqDesc, setReqDesc] = useState('')
  const [reqPriority, setReqPriority] = useState('NORMAL')
  const [reqUserId, setReqUserId] = useState<number | undefined>(undefined)
  const [userFormWarning, setUserFormWarning] = useState<string | null>(null)
  const [requestFormWarning, setRequestFormWarning] = useState<string | null>(null)
  const [confirmModal, setConfirmModal] = useState<{title:string; onConfirm:()=>void} | null>(null)
  useEffect(()=>{ fetchList() }, [])
  async function fetchList(){
    try{
      const res = await axios.get('/api/requests')
      setRequests(res.data)
      const ur = await axios.get('/api/users')
      setUsers(ur.data)
    }catch(e){ console.error(e) }
  }
  async function createUser(e: React.FormEvent){
    e.preventDefault()
    if (!name || !email) { setUserFormWarning('Name and email are required'); return }
    try{
      const res = await axios.post('/api/users', { name, email })
      await fetchList()
      setName('')
      setEmail('')
    }catch(err){ console.error(err); setUserFormWarning('Failed to create user') }
  }
  async function startEditUser(u: User){
    setEditingUserId(u.id || null)
    setName(u.name)
    setEmail(u.email)
  }
  async function saveUser(){
    if (!editingUserId) return
    try{
      const res = await axios.put(`/api/users/${editingUserId}`, { name, email })
      await fetchList()
      setEditingUserId(null)
      setName('')
      setEmail('')
    }catch(e){ console.error(e); alert('Failed to update user') }
  }
  async function deleteUser(id?: number){
    if (!id) return
    setConfirmModal({ title: 'Delete user?', onConfirm: async ()=>{
      try{
        await axios.delete(`/api/users/${id}`)
        await fetchList()
      }catch(e){ console.error(e); setUserFormWarning('Failed to delete user') }
      setConfirmModal(null)
    } })
  }
  async function createRequest(e: React.FormEvent){
    e.preventDefault()
    if (!reqUserId) { setRequestFormWarning('Select a user'); return }
    try{
      const res = await axios.post('/api/requests', { userId: reqUserId, type: reqType, description: reqDesc, priority: reqPriority })
      await fetchList()
      setReqDesc('')
      setRequestFormWarning(null)
    }catch(e){ console.error(e); setRequestFormWarning('Failed to create request') }
  }
  async function createUser(e: React.FormEvent){
    e.preventDefault()
    try{
      const res = await axios.post('/api/users', { name, email })
      setUsers(prev => [...prev, res.data])
      setName('')
      setEmail('')
    }catch(err){ console.error(err) }
  }
  return (
    <div style={{padding:20}}>
      <h1>PatternHub</h1>
      <h2>Requests</h2>
      <ul>
        {requests.map(r => <li key={r.id}>{r.description} ({r.priority})</li>)}
      </ul>

      <h2>Users</h2>
      <form onSubmit={editingUserId ? (e)=>{e.preventDefault(); saveUser()} : createUser} style={{marginBottom:12}} className="form-row">
        <input placeholder="Name" value={name} onChange={e=>setName(e.target.value)} />
        <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} style={{marginLeft:8}} />
        <button type="submit" style={{marginLeft:8}}>{editingUserId ? 'Save' : 'Create'}</button>
        {editingUserId && <button type="button" onClick={()=>{setEditingUserId(null); setName(''); setEmail('')}} style={{marginLeft:8}}>Cancel</button>}
      </form>
      {userFormWarning && <div className="warning">{userFormWarning}</div>}

      <table className="table">
        <thead>
          <tr><th>ID</th><th>Name</th><th>Email</th><th>Actions</th></tr>
        </thead>
        <tbody>
          {users.map(u => (
            <tr key={u.id}>
              <td>{u.id}</td>
              <td>{u.name}</td>
              <td>{u.email}</td>
              <td>
                <button onClick={()=>startEditUser(u)}>Edit</button>
                <button onClick={()=>deleteUser(u.id)} style={{marginLeft:8}}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2>Requests</h2>
      <form onSubmit={createRequest} style={{marginBottom:12}} className="form-row">
        <select value={reqUserId ?? ''} onChange={e=>setReqUserId(e.target.value ? Number(e.target.value) : undefined)}>
          <option value=''>Select user</option>
          {users.map(u=> <option key={u.id} value={u.id}>{u.name}</option>)}
        </select>
        <input placeholder="Type" value={reqType} onChange={e=>setReqType(e.target.value)} style={{marginLeft:8}} />
        <input placeholder="Description" value={reqDesc} onChange={e=>setReqDesc(e.target.value)} style={{marginLeft:8, width:300}} />
        <select value={reqPriority} onChange={e=>setReqPriority(e.target.value)} style={{marginLeft:8}}>
          <option value="NORMAL">NORMAL</option>
          <option value="PRIORITY">PRIORITY</option>
          <option value="URGENT">URGENT</option>
        </select>
        <button type="submit" style={{marginLeft:8}}>Create Request</button>
      </form>
      {requestFormWarning && <div className="warning">{requestFormWarning}</div>}

      <table className="table">
        <thead>
          <tr><th>ID</th><th>User</th><th>Type</th><th>Description</th><th>Priority</th><th>Status</th></tr>
        </thead>
        <tbody>
          {requests.map(r => (
            <tr key={r.id}>
              <td>{r.id}</td>
              <td>{r.user?.name ?? r.user?.email ?? '—'}</td>
              <td>{r.type}</td>
              <td>{r.description}</td>
              <td>{r.priority}</td>
              <td>{r.status}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {confirmModal && (
        <div className="modal-backdrop">
          <div className="modal">
            <div>{confirmModal.title}</div>
            <div style={{textAlign:'right', marginTop:12}}>
              <button onClick={()=>setConfirmModal(null)}>Cancel</button>
              <button onClick={()=>{ confirmModal.onConfirm(); }} style={{marginLeft:8}}>Confirm</button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
