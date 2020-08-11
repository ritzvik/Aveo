import React, { useState, useRef, useEffect } from 'react';
import { BrowserRouter as Router, Switch, Route, Redirect } from 'react-router-dom'
import MonthView from './MonthView'
import Nav from './Nav'

const LOCAL_STORAGE_KEY = 'ta.tid'

function App() {
  const [tid, setTid] = useState(null)
  const tidRef = useRef()

  useEffect(() => {
    const savedTid = JSON.parse(
      localStorage.getItem(LOCAL_STORAGE_KEY)
    )
    if (savedTid) setTid(savedTid)
  }, [])

  useEffect(() => {
    localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(tid))
    console.log("New TeacherID", tid)
  }, [tid])

  function handleTidEntry(e) {
    if (tidRef.current.value == null) return;
    const newTid = tidRef.current.value
    setTid(newTid)
    tidRef.current.value = null
    return <Redirect to="/month" />
  }

  return (
    <Router>
      <Nav />
      <Switch>
        <Route path="/" exact>
          <input ref={tidRef} type="number" />
          <button onClick={handleTidEntry}>Set TeacherID</button>
          <button onClick={event => window.location.href = "/month"}>Go!</button>
        </Route>
        <Route path="/month" exact>
          <MonthView tid={tid} />
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
