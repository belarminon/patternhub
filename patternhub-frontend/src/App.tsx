import React, {useEffect, useState} from 'react'
import axios from 'axios'

type Request = {
  id?: number
  description: string
  priority: string
  type: string
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
      <form onSubmit={createUser} style={{marginBottom:12}}>
        <input placeholder="Name" value={name} onChange={e=>setName(e.target.value)} />
        <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} style={{marginLeft:8}} />
        <button type="submit" style={{marginLeft:8}}>Create</button>
      </form>
      <ul>
        {users.map(u => <li key={u.id}>{u.name} &lt;{u.email}&gt;</li>)}
      </ul>
    </div>
  )
}
