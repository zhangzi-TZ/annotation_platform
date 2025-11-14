import { Navigate, Route, Routes } from 'react-router-dom';
import SessionListPage from './pages/SessionListPage';
import SessionDetailPage from './pages/SessionDetailPage';

function App() {
  return (
    <Routes>
      <Route path="/" element={<SessionListPage />} />
      <Route path="/sessions/:sessionId" element={<SessionDetailPage />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;
