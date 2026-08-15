import React, {useEffect, useState} from 'react'
import axios from 'axios'

type Request = {
  id?: number
  description: string
  priority: string
  type: string
}

export default function App(){
  const [requests, setRequests] = useState<Request[]>([])
  useEffect(()=>{ fetchList() }, [])
  async function fetchList(){
    try{
      const res = await axios.get('/api/requests')
      setRequests(res.data)
    }catch(e){ console.error(e) }
  }
  return (
    <div style={{padding:20}}>
      <h1>PatternHub</h1>
      <h2>Requests</h2>
      <ul>
        {requests.map(r => <li key={r.id}>{r.description} ({r.priority})</li>)}
      </ul>
    </div>
  )
}
